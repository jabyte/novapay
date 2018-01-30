package ng.novateur.novapay;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import wangpos.sdk4.libbasebinder.BankCard;
import wangpos.sdk4.libbasebinder.Core;

public class CardDetailsEntry extends AppCompatActivity {
    private static final String TAG = Class.class.getSimpleName();
    private Core core;
    private BankCard card;
    private CardReader cardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details_entry);

        Button abortBtn = (Button) findViewById(R.id.abort_btn);
//
//        new Thread() {
//            @Override
//            public void run() {
//                core = new Core(getApplicationContext());
//                card = new BankCard(getApplicationContext());
//                cardReader = new CardReader(core, card);
//            }
//
//        }.start();
//        String cardDetails;
//        cardDetails = cardReader.readCard();
//        Toast.makeText(this, cardDetails, Toast.LENGTH_LONG).show();
    }


    public void readCard() {
        try {

            Intent parent = getIntent();
            parent.putExtra("CardNumber", cardReader.getCardNumber());
            parent.putExtra("CardExpYear", cardReader.getCardExpiryYear());
            parent.putExtra("CardExpMonth", cardReader.getCardExpiryMonth());
            parent.putExtra("CardCVV", cardReader.getCardCVV());

            setResult(RESULT_OK, parent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "Aaaaaagh......!!!.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onAbortButtonClicked(View view) {
        if (view.getId() == R.id.abort_btn) {
            Toast.makeText(this, "Transaction aborted.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}