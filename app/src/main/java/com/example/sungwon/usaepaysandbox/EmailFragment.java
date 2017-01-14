package com.example.sungwon.usaepaysandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ebizcharge.sdk.Gateway;
import com.ebizcharge.sdk.classes.OnGatewayResultListener;
import com.ebizcharge.sdk.classes.TransactionResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SungWon on 1/12/2017.
 */
public class EmailFragment extends Fragment {
    private static final String EXTRA_RESPONSE = "extraResponse";

    private EditText mEmail;

    private TransactionResponse mResponse;

    /**
     * Do not call this default constructor. Use {@code EmailFragment.newInstance} instead.
     */
    public EmailFragment() {
        // do nothing
    }

    public static final EmailFragment newInstance(TransactionResponse response) {
        EmailFragment fragment = new EmailFragment();

        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_RESPONSE, response);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResponse = getArguments().getParcelable(EXTRA_RESPONSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment_library_fragment_email, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = (EditText) view.findViewById(R.id.payment_library_email_address);
        Button send = (Button) view.findViewById(R.id.payment_library_send);
        Button cancel = (Button) view.findViewById(R.id.payment_library_cancel);

        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putParcelable(PaymentLibraryConstants.TRANSACTION_RESPONSE, mResponse);
                returnResult(Gateway.RESULT_OK, b);
            }
        });
    }

    private void sendEmail() {
        if (!isInputValid()) {
            return;
        }

        Gateway gateway = new Gateway(PaymentLibraryConstants.SOURCE_KEY,
                PaymentLibraryConstants.SOURCE_PIN, mListener);
        gateway.setSandboxMode(PaymentLibraryConstants.SANDBOX_MODE);
        gateway.emailReceipt(mResponse.getRefNum(), mEmail.getText().toString(), "tranapi_customer");
    }

    /**
     * Returns {@code true} if email address is in a valid format, otherwise {@code false}. A toast
     * will alert the user which input is invalid.
     */
    private boolean isInputValid() {
        String email = mEmail.getText().toString();
        Pattern p = Pattern.compile("^[\\w.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"); // basic pattern
        Matcher m = p.matcher(email);
        if (!m.find()) {
            Toast.makeText(getActivity(),
                    getString(R.string.payment_library_invalid_email_address), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    /**
     * A callback for the Gateway when the transaction is finished processing.
     */
    private OnGatewayResultListener mListener = new OnGatewayResultListener() {
        @Override
        public void onGatewayResult(int requestCode, int resultCode, Bundle data) {
            Bundle b = new Bundle();

            if (resultCode == Gateway.RESULT_OK) {
                b.putParcelable(PaymentLibraryConstants.TRANSACTION_RESPONSE, mResponse);
            } else { // resultCode == Gateway.RESULT_ERROR
                b.putParcelable(PaymentLibraryConstants.GATEWAY_EXCEPTION,
                        data.getParcelable(Gateway.EXTRA_EXCEPTION));
            }

            returnResult(resultCode, b);
        }
    };

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
