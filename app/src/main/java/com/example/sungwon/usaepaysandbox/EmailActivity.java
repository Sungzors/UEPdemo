package com.example.sungwon.usaepaysandbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ebizcharge.sdk.classes.TransactionResponse;

/**
 * Created by SungWon on 1/12/2017.
 */
public class EmailActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_library_activity);

        TransactionResponse response = getIntent().getParcelableExtra(
                PaymentLibraryConstants.TRANSACTION_RESPONSE);

        // null check
        if (response == null) {
            throw new NullPointerException(
                    "The transaction response is missing! Make sure the intent extra key is PaymentLibraryConstants.TRANSACTION_RESPONSE.");
        }

        Fragment fragment = EmailFragment.newInstance(response);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
