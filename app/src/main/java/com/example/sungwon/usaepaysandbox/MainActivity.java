package com.example.sungwon.usaepaysandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ebizcharge.sdk.classes.CurrencyAmount;
import com.ebizcharge.sdk.classes.TransactionRequest;
import com.ebizcharge.sdk.classes.TransactionResponse;
import com.ebizcharge.sdk.enums.Command;
import com.ebizcharge.sdk.exception.GatewayException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Main";
    String SourceKey = "_95k469ivih82RKXPv7T9dE44vKHR71Y";
    String Pin = "p6wfNC";
    String ClientIP = "127.0.0.1";

    private static final int REQUEST_PAYMENT_LIBRARY = 0;

    String mOption = "Sale";

    EditText mCardHolder;
    EditText mCardNumber;
    EditText mExpiration;
    EditText mChargeAmount;
    EditText mInvoice;
    EditText mTaxamt;
    EditText mPonum;
    EditText mDescription;
    EditText mCustomerid;
    EditText mOrderid;
    EditText mBillingst;
    EditText mBillingzip;
    EditText mCvv2cvc;

    Button mSubmitButton;

    LinearLayout mContainer;

    Spinner mOptionDropDown;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            setupUEPStuff();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSubmitButton = (Button)findViewById(R.id.submitButton);
        mContainer = (LinearLayout)findViewById(R.id.transactionContainer);
        mOptionDropDown = (Spinner)findViewById(R.id.typeofTrans);
        ArrayList<String> list = new ArrayList<>();
        list.add(0, "Sale");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(list);
        mOptionDropDown.setAdapter(adapter);
        mOptionDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOption = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mOption = "Sale";
            }
        });
        setupViews();
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PAYMENT_LIBRARY) {
            TransactionResponse response = data
                    .getParcelableExtra(PaymentLibraryConstants.TRANSACTION_RESPONSE);

            if (response == null) { // something wrong happened when communicating with the gateway
                GatewayException exception = data
                        .getParcelableExtra(PaymentLibraryConstants.GATEWAY_EXCEPTION);
                exception.printStackTrace();
            } else {
                String msg = "Transaction #: " + response.getRefNum() + ", Result: "
                        + response.getResult() +", Error: " + response.getError();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                Log.d(TAG,msg);
            }
        }
    }

    public void setupUEPStuff() throws Exception {
//        client = usaepay.getClient("sandbox.usaepay.com/gate");

//        UsaepayService usap = new UsaepayService();
//        client = usap.getUeSoapServerPort();
//        URL url = new URL("sandbox.usaepay.com/gate");
//        usaepay.setAddress(client, url);

//        token = usaepay.getToken(SourceKey, Pin, ClientIP);1234
    }

    public void setupViews(){
        switch (mOption){

            case("Sale"):
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View inflatedlayout = inflater.inflate(R.layout.sales, null, false);
                mContainer.addView(inflatedlayout);
                mCardHolder = (EditText)findViewById(R.id.cardHolder);
                mCardNumber = (EditText)findViewById(R.id.cardNumber);
                mExpiration = (EditText)findViewById(R.id.expiration);
                mChargeAmount = (EditText)findViewById(R.id.chargeAmount);
                mInvoice = (EditText)findViewById(R.id.invoice);
                mTaxamt = (EditText)findViewById(R.id.taxamt);
                mPonum = (EditText)findViewById(R.id.ponum);
                mDescription = (EditText)findViewById(R.id.description);
                mCustomerid = (EditText)findViewById(R.id.customerid);
                mOrderid = (EditText)findViewById(R.id.orderid);
                mBillingst = (EditText)findViewById(R.id.billingst);
                mBillingzip = (EditText)findViewById(R.id.billingzip);
                mCvv2cvc = (EditText)findViewById(R.id.cvv2cvc);
//                autoNext(mCardHolder, mCardNumber);
//                autoNext(mCardNumber, mExpiration);
//                autoNext(mExpiration, mChargeAmount);
//                autoNext(mChargeAmount, mInvoice);
//                autoNext(mInvoice, mTaxamt);
//                autoNext(mTaxamt, mPonum);
//                autoNext(mPonum, mDescription);
//                autoNext(mDescription, mCustomerid);
//                autoNext(mCustomerid, mOrderid);
//                autoNext(mOrderid, mBillingst);
//                autoNext(mBillingst, mBillingzip);
//                autoNext(mBillingzip, mCvv2cvc);
                break;

        }
    }

    public void createTransaction() {
        switch(mOption){
            case("Sale"):
                TransactionRequest request = new TransactionRequest();
                    request.setAccountHolder(mCardHolder.getText().toString());

//                if(!TextUtils.isEmpty(mCustomerid.getText().toString())){
//                    request.set(mCustomerid.getText().toString());
//                }

                CurrencyAmount amt = new CurrencyAmount(mChargeAmount.getText().toString());
                CurrencyAmount tax = new CurrencyAmount(Math.round(Double.valueOf(mTaxamt.getText().toString())), 0);
                request.setCommand(Command.CC_SALE);

                    amt.add(tax);
                request.setTotal(amt.add(tax));
                request.setTax(tax);
                request.setInvoice(mInvoice.getText().toString());


//                if(!TextUtils.isEmpty(mDescription.getText().toString())){
//                    details.setDescription(mDescription.getText().toString());
//                }
//                if(!TextUtils.isEmpty(mInvoice.getText().toString())){
//                    request.setInvoice(mInvoice.getText().toString());
//                }
//                if(!TextUtils.isEmpty(mPonum.getText().toString())){
//                    details.setPONum(mPonum.getText().toString());
//                }
//                if(!TextUtils.isEmpty(mOrderid.getText().toString())){
//                    details.setOrderID(mOrderid.getText().toString());
//                }

//                request.setDetails(details);
//
//                CreditCardData ccdata = new CreditCardData();
//                if(!TextUtils.isEmpty(mBillingst.getText().toString())){
//                    ccdata.setAvsStreet(mBillingst.getText().toString());
//                }
//                if(!TextUtils.isEmpty(mBillingzip.getText().toString())){
//                    ccdata.setAvsZip(mBillingzip.getText().toString());
//                }
//                if(mCardNumber.getText().toString().trim().equals("")){
//                    mCardNumber.setError("Card Number Required");
//                }else{
//                    ccdata.setCardNumber(mCardNumber.getText().toString());
//                }
//                if(mExpiration.getText().toString().trim().equals("")){
//                    mExpiration.setError("Expiration Date (MMYY) Required");
//                }else{
//                    ccdata.setCardExpiration(mExpiration.getText().toString());
//                }
//                if(!TextUtils.isEmpty(mCvv2cvc.getText().toString())){
//                    ccdata.setCardCode(mCvv2cvc.getText().toString());
//                }
//                request.setCreditCardData(ccdata);
//
//                TransactionResponse response;
//
//                try {
//                    response = client.runSale(token, request);
//                    //turn into alertdialog
//                    Log.d(TAG, "Response: " + response.getResult() + " RefNum: " + response.getRefNum());
//                } catch (GeneralFault_Exception e) {
//                    e.printStackTrace();
//                }
//
                Intent i = new Intent(this, PaymentLibraryActivity.class);
                i.putExtra(PaymentLibraryConstants.TRANSACTION_REQUEST, request);
                startActivityForResult(i, REQUEST_PAYMENT_LIBRARY);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case(R.id.submitButton):
                createTransaction();
                break;
        }
    }

    public void autoNext(final EditText view1, final EditText view2){
        view1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_UP)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    view1.clearFocus();
                    view2.requestFocus();
                }
                return false;
            }
        });
    }

}
