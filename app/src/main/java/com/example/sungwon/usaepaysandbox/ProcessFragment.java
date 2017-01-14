package com.example.sungwon.usaepaysandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebizcharge.sdk.Gateway;
import com.ebizcharge.sdk.classes.OnGatewayResultListener;
import com.ebizcharge.sdk.classes.TransactionRequest;
import com.ebizcharge.sdk.classes.TransactionResponse;
import com.ebizcharge.sdk.enums.Command;

/**
 * Created by SungWon on 1/12/2017.
 */
public class ProcessFragment extends Fragment{
    private static final String EXTRA_REQUEST = "extraRequest";

    private TransactionRequest mRequest;

    /**
     * Do not call this default constructor. Use {@code ProcessFragment.newInstance} instead.
     */
    public ProcessFragment() {
        // do nothing
    }

    /**
     * @param request - the details of the transaction
     * @return a new {@code ResultFragment} instance
     */
    public static final ProcessFragment newInstance(TransactionRequest request) {
        ProcessFragment fragment = new ProcessFragment();

        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_REQUEST, request);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequest = getArguments().getParcelable(EXTRA_REQUEST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payment_library_fragment_process, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Gateway gateway = new Gateway(PaymentLibraryConstants.SOURCE_KEY,
                PaymentLibraryConstants.SOURCE_PIN, mListener);
        gateway.setSandboxMode(PaymentLibraryConstants.SANDBOX_MODE);
        gateway.runTransaction(mRequest);
    }

    /**
     * A callback for the Gateway when the transaction is finished processing.
     */
    private OnGatewayResultListener mListener = new OnGatewayResultListener() {
        @Override
        public void onGatewayResult(int requestCode, int resultCode, Bundle data) {
            if (resultCode == Gateway.RESULT_OK) {
                TransactionResponse response = data.getParcelable(Gateway.EXTRA_RESULT);

                // display the signature screen for credit card transactions
                if (mRequest.getCommand() == Command.CC_SALE
                        || mRequest.getCommand() == Command.CC_AUTH) {
                    showSignatureScreen(response);
                } else {
                    showEmailReceiptScreen(response);
                }
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
     * Displays the signature screen.
     */
    private void showSignatureScreen(TransactionResponse response) {
        Intent i = new Intent(getActivity(), SignatureActivity.class);
        i.putExtra(PaymentLibraryConstants.TRANSACTION_RESPONSE, response);
        i.putExtra(PaymentLibraryConstants.TRANSACTION_REQUEST, mRequest);

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
