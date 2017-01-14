package com.example.sungwon.usaepaysandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ebizcharge.sdk.classes.TransactionRequest;

/**
 * Created by SungWon on 1/12/2017.
 */

public class PaymentLibraryActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_library_activity);

        TransactionRequest request = getIntent().getParcelableExtra(
                PaymentLibraryConstants.TRANSACTION_REQUEST);

        // null check
        if (request == null) {
            throw new NullPointerException(
                    "The transaction request is missing! Make sure the intent extra key is PaymentLibraryConstants.TRANSACTION_REQUEST.");
        }
        if (request.getCommand() == null) {
            throw new NullPointerException("The transaction request command cannot be null!");
        }

        // determine which payment fragment to call
        Fragment fragment = null;
        switch (request.getCommand()) {
            case CC_SALE:
            case CC_AUTH:
                fragment = CreditCardFragment.newInstance(request);
                break;
//            case CASH_SALE:
//                fragment = CashFragment.newInstance(request);
//                break;
//            case CHECK_SALE:
//                fragment = CheckFragment.newInstance(request);
//                break;
//            case CHECK_ACH:
//                fragment = AchFragment.newInstance(request);
//                break;
            default:
                throw new IllegalStateException(
                        "Currently unsupported TransactionRequest command: "
                                + request.getCommand().toString());
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
