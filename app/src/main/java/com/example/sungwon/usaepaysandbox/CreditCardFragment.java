package com.example.sungwon.usaepaysandbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ebizcharge.sdk.classes.CreditCardData;
import com.ebizcharge.sdk.classes.CurrencyAmount;
import com.ebizcharge.sdk.classes.TransactionRequest;
import com.ebizcharge.sdk.enums.Command;

/**
 * Created by SungWon on 1/12/2017.
 */
public class CreditCardFragment extends Fragment {
    private static final String EXTRA_REQUEST = "extraRequest";

    private TextView mTotal;
    private EditText mName;
    private EditText mNumber;
    private EditText mExp;
    private EditText mCvv;
    private EditText mStreet;
    private EditText mZip;
    private Button mSend;

    private TransactionRequest mRequest;

    /**
     * Do not call this default constructor. Use one of the {@code CreditCardFragment.newInstance}
     * methods instead.
     */
    public CreditCardFragment() {
        // do nothing
    }

    /**
     * Constructs a new instance. Use this constructor when only basic transaction details are
     * needed (total, invoice).
     *
     * @param total amount of this transaction
     * @param invoice number of this transaction
     * @param authOnly process this transaction as an authorization only, not a sale
     * @return a new {@code CreditCardFragment} instance
     */
    public static final CreditCardFragment newInstance(CurrencyAmount total, String invoice,
                                                       boolean authOnly) {
        TransactionRequest request = new TransactionRequest();
        request.setCommand(authOnly ? Command.CC_AUTH : Command.CC_SALE);
        request.setTotal(total);
        request.setInvoice(invoice);

        return newInstance(request);
    }

    /**
     * Constructs a new instance. The {@code TransactionRequest} that is passed through this
     * constructor will be sent to the gateway.
     *
     * @param request - the details of the transaction
     * @return a new {@code CreditCardFragment} instance
     * @throws IllegalArgumentException if {@code request.getCommand()} is not either
     *             {@code Command.CC_SALE} or {@code Command.CC_AUTH}
     */
    public static final CreditCardFragment newInstance(TransactionRequest request) {
        // check if request command is appropriate
        if (request.getCommand() != Command.CC_SALE && request.getCommand() != Command.CC_AUTH) {
            throw new IllegalArgumentException(
                    "Command for transaction request must be either Command.CC_SALE or Command.CC_AUTH: "
                            + request.getCommand().toString());
        }

        CreditCardFragment fragment = new CreditCardFragment();

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
        return inflater.inflate(R.layout.payment_library_fragment_creditcard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTotal = (TextView) view.findViewById(R.id.payment_library_total);
        mName = (EditText) view.findViewById(R.id.payment_library_cc_name);
        mNumber = (EditText) view.findViewById(R.id.payment_library_cc_number);
        mExp = (EditText) view.findViewById(R.id.payment_library_cc_expiration);
        mCvv = (EditText) view.findViewById(R.id.payment_library_cc_cvv);
        mStreet = (EditText) view.findViewById(R.id.payment_library_avs_street);
        mZip = (EditText) view.findViewById(R.id.payment_library_avs_zip);
        mSend = (Button) view.findViewById(R.id.payment_library_pay);

        mTotal.setText(mRequest.getTotal().toCurrencyString());

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTransaction();
            }
        });
    }

    /**
     * Sends the transaction to the gateway with user-input credit card details.
     */
    private void sendTransaction() {
        if (!isInputValid()) {
            return;
        }

        CreditCardData ccd = new CreditCardData();
        ccd.setCardNumber(mNumber.getText().toString());
        ccd.setCardExpiration(mExp.getText().toString());
        ccd.setCardCode(mCvv.getText().toString());
        ccd.setAvsStreet(mStreet.getText().toString());
        ccd.setAvsZip(mZip.getText().toString());

        mRequest.setCreditCardData(ccd);
        mRequest.setAccountHolder(mName.getText().toString());

        // send data to next fragment for processing
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ProcessFragment.newInstance(mRequest)).commit();
    }

    /**
     * Checks if a name is entered and if the credit card number, expiration and cvv are valid.
     * Returns {@code false} if any of these are invalid. A toast will alert the user which input is
     * invalid.
     */
    private boolean isInputValid() {
        if (mName.length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.payment_library_invalid_card_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CreditCardData.validateNumber(mNumber.getText().toString())) {
            Toast.makeText(getActivity(), getString(R.string.payment_library_invalid_card_number),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CreditCardData.validateExpiration(mExp.getText().toString())) {
            Toast.makeText(getActivity(),
                    getString(R.string.payment_library_invalid_card_expiration), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (mCvv.length() < 3) {
            Toast.makeText(getActivity(), getString(R.string.payment_library_invalid_card_cvv),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
