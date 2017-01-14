package com.example.sungwon.usaepaysandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ebizcharge.sdk.classes.TransactionRequest;
import com.ebizcharge.sdk.classes.TransactionResponse;

/**
 * Created by SungWon on 1/12/2017.
 */
public class SignatureActivity extends FragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_library_activity);

        TransactionRequest request = getIntent().getParcelableExtra(
                PaymentLibraryConstants.TRANSACTION_REQUEST);
        TransactionResponse response = getIntent().getParcelableExtra(
                PaymentLibraryConstants.TRANSACTION_RESPONSE);

        // null check
        if (request == null) {
            throw new NullPointerException(
                    "The transaction request is missing! Make sure the intent extra key is PaymentLibraryConstants.TRANSACTION_REQUEST.");
        }
        if (response == null) {
            throw new NullPointerException(
                    "The transaction response is missing! Make sure the intent extra key is PaymentLibraryConstants.TRANSACTION_RESPONSE.");
        }
        if (request.getCommand() == null) {
            throw new NullPointerException("The transaction request command cannot be null!");
        }

        Fragment fragment = SignatureFragment.newInstance(response.getRefNum(), request);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
