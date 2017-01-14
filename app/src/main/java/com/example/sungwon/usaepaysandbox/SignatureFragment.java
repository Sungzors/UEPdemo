package com.example.sungwon.usaepaysandbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ebizcharge.sdk.Gateway;
import com.ebizcharge.sdk.classes.OnGatewayResultListener;
import com.ebizcharge.sdk.classes.TransactionRequest;
import com.ebizcharge.sdk.classes.TransactionResponse;

import java.io.ByteArrayOutputStream;

/**
 * Created by SungWon on 1/12/2017.
 */
public class SignatureFragment extends Fragment{
    private static final String EXTRA_REFNUM = "extraRefNum";
    private static final String EXTRA_REQUEST = "extraRequest";

    private static final int MAX_IMAGE_SIZE = 240; // px

    private DrawableImageView mSignature;

    private TransactionRequest mRequest;
    private long mRefNum;

    /**
     * Do not call this default constructor. Use {@code SignatureFragment.newInstance} instead.
     */
    public SignatureFragment() {
        // do nothing
    }

    public static final SignatureFragment newInstance(long transactionRefNum,
                                                      TransactionRequest request) {
        SignatureFragment fragment = new SignatureFragment();

        final Bundle args = new Bundle();
        args.putLong(EXTRA_REFNUM, transactionRefNum);
        args.putParcelable(EXTRA_REQUEST, request);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequest = getArguments().getParcelable(EXTRA_REQUEST);
        mRefNum = getArguments().getLong(EXTRA_REFNUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment_library_fragment_signature, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignature = (DrawableImageView) view.findViewById(R.id.payment_library_signature);
        TextView total = (TextView) view.findViewById(R.id.payment_library_total);
        Button accept = (Button) view.findViewById(R.id.payment_library_accept);
        Button clear = (Button) view.findViewById(R.id.payment_library_clear);

        total.setText(mRequest.getTotal().toCurrencyString());

        accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignature();
            }
        });

        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature.clearView();
            }
        });
    }

    private void sendSignature() {
        Bitmap bm = getSignatureImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        mRequest.getCreditCardData().setSignature(encodeBase64(bytes));

        Gateway gateway = new Gateway(PaymentLibraryConstants.SOURCE_KEY,
                PaymentLibraryConstants.SOURCE_PIN, mListener);
        gateway.setSandboxMode(PaymentLibraryConstants.SANDBOX_MODE);
        gateway.captureTransaction(mRefNum, mRequest);
    }

    private Bitmap getSignatureImage() {
        return saveView(mSignature);
    }

    private Bitmap saveView(View view) {
        int w = view.getWidth();
        int h = view.getHeight();

        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        c.drawColor(Color.WHITE);
        view.draw(c);

        if (w > h) {
            h = MAX_IMAGE_SIZE * h / w;
            w = MAX_IMAGE_SIZE;
        } else {
            w = MAX_IMAGE_SIZE * w / h;
            h = MAX_IMAGE_SIZE;
        }

        return Bitmap.createScaledBitmap(bm, w, h, false);
    }

    private String encodeBase64(byte[] array) {
        String encodingTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        int size = array.length;
        if (size == 0) {
            return "";
        }

        char[] characters = new char[((size + 2) / 3) * 4];

        int length = 0;

        int i = 0;
        while (i < size) {
            byte buffer[] = {0, 0, 0};
            short bufferLength = 0;
            while (bufferLength < 3 && i < size)
                buffer[bufferLength++] = array[i++];

            // encode the bytes in the buffer to four characters, including
            // padding "=" characters if necessary.
            characters[length++] = encodingTable.charAt((buffer[0] & 0xFC) >> 2);
            characters[length++] = encodingTable.charAt(((buffer[0] & 0x03) << 4)
                    | ((buffer[1] & 0xF0) >> 4));
            if (bufferLength > 1) {
                characters[length++] = encodingTable.charAt(((buffer[1] & 0x0F) << 2)
                        | ((buffer[2] & 0xC0) >> 6));
            } else {
                characters[length++] = '=';
            }

            if (bufferLength > 2) {
                characters[length++] = encodingTable.charAt(buffer[2] & 0x3F);
            } else {
                characters[length++] = '=';
            }
        }

        String result = new String(characters);

        return result;
    }

    /**
     * A callback for the Gateway when the transaction is finished processing.
     */
    private OnGatewayResultListener mListener = new OnGatewayResultListener() {
        @Override
        public void onGatewayResult(int requestCode, int resultCode, Bundle data) {
            if (resultCode == Gateway.RESULT_OK) {
                TransactionResponse response = data.getParcelable(Gateway.EXTRA_RESULT);

                showEmailReceiptScreen(response);
            } else { // resultCode == Gateway.RESULT_ERROR
                Bundle b = new Bundle();
                b.putParcelable(PaymentLibraryConstants.GATEWAY_EXCEPTION,
                        data.getParcelable(Gateway.EXTRA_EXCEPTION));

                returnResult(resultCode, b);
            }

        }
    };

    /**
     * Displays the email receipt screen.
     */
    private void showEmailReceiptScreen(TransactionResponse response) {
        Intent i = new Intent(getActivity(), EmailActivity.class);
        i.putExtra(PaymentLibraryConstants.TRANSACTION_RESPONSE, response);

        startActivityForResult(i, 0);
    }

    /**
     * Sets the transaction result for the parent activity to return, and finish the activity.
     *
     * @param resultCode - a Gateway integer indicating if the transaction was successfully sent to
     *            the gateway.
     * @param data - a Bundle that contains a {@code TransactionResponse} parcelable extra if
     *            successful, a {@code GatewayException} parcelable extra otherwise.
     */
    private void returnResult(int resultCode, Bundle data) {
        Intent i = new Intent();
        i.putExtras(data);

        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }

}
