package ng.novateur.novapay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import wangpos.sdk4.libbasebinder.BankCard;
import wangpos.sdk4.libbasebinder.Core;

public class MainActivity extends AppCompatActivity {

    private static final int AMOUNT_ENTRY = 1;
    private static final int CARD_DETAILS_ENTRY = 2;
    private static final int CARD_PIN_ENTRY = 3;
    private static final String TAG = Class.class.getSimpleName();

    private Card card;
    private Charge charge;

    private Intent child;

    // Transaction specific variables;
    private int amount;
    private String cardNumber;
    private int cardExpiryMonth;
    private int cardExpiryYear;
    private String cardPin;
    private String cardCVV;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PaystackSdk.initialize(getApplicationContext());
    }

    public void onMakePaymentBtnClick(View view) {
        child = new Intent(MainActivity.this, AmountEntryActivity.class);
        startActivityForResult(child, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AMOUNT_ENTRY:
                    if (data.hasExtra("Amount")) {
                        amount = data.getIntExtra("Amount", 0);

                        child = new Intent(MainActivity.this, CardDetailsEntry.class);
                        startActivityForResult(child, CARD_DETAILS_ENTRY);
                    }
                    break;

                case CARD_DETAILS_ENTRY:
                    if (data.hasExtra("CardNumber") && data.hasExtra("CardExpMonth") && data.hasExtra("CardExpYear") && data.hasExtra("CardCVV")) {
                        cardNumber = data.getStringExtra("CardNumber");
                        cardExpiryMonth = Integer.parseInt(data.getStringExtra("CardExpMonth"));
                        cardExpiryYear = Integer.parseInt(data.getStringExtra("CardExpYear")) + 2000;
                        cardCVV = data.getStringExtra("CardCVV");

                        Toast.makeText(this, String.valueOf(cardExpiryMonth) + "\n" + String.valueOf(cardExpiryYear), Toast.LENGTH_LONG).show();
//                        Toast.makeText(this, data.getStringExtra("CardExpMonth") + "\n" + data.getStringExtra("CardExpYear"), Toast.LENGTH_LONG).show();
                        card = new Card(cardNumber, cardExpiryMonth, cardExpiryYear, cardCVV);
                        performTransaction(card, amount);
                    }
                    else
                    {
                        Toast.makeText(this, "Missing data from the card!", Toast.LENGTH_LONG).show();
                    }
                    break;

                case CARD_PIN_ENTRY:
                    if (!data.hasExtra("CardPin")) {
                        return;
                    }

                    cardPin = data.getStringExtra("CardPin");
                    break;
            }
        } else {
            Toast.makeText(this, "An error occured! Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void performTransaction(Card card, int amount) {
        charge = new Charge();
        charge.setAmount(amount);
        charge.setCard(card);
        //charge.setPlan("Normal");
        charge.setEmail("jabyte@novateur.ng");

        try {
            if (card.isValid()) {

                PaystackSdk.chargeCard(MainActivity.this, charge, new Paystack.TransactionCallback() {

                    @Override
                    public void onSuccess(Transaction transaction) {
                        Toast.makeText(MainActivity.this, "SUCCESS\n" + transaction.getReference(), Toast.LENGTH_LONG).show();
                        printReceipt( transaction );
                    }

                    @Override
                    public void beforeValidate(Transaction transaction) {
                        child = new Intent(MainActivity.this, PinEntryActivity.class);
                        startActivityForResult(child, CARD_PIN_ENTRY);
                    }

                    @Override
                    public void onError(Throwable error, Transaction transaction) {
                        Toast.makeText(MainActivity.this, "ERR:\n" + error.getMessage(), Toast.LENGTH_LONG).show();
                        printReceipt( transaction );
                    }
                });

            } else {
                Toast.makeText(MainActivity.this, "Cannot Verify Card!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }
    }

    public void printReceipt(Transaction transaction){
        Toast.makeText(this, transaction.getReference(), Toast.LENGTH_LONG).show();
    }
}