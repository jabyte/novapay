package ng.novateur.novapay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PinEntryActivity extends AppCompatActivity {

    private static final String TAG = Class.class.getSimpleName();

    private String cardPin;

    private TextView cardPinTv;
    private Button proceedBtn;
    private Button cancelBtn;

    // Keypad initialization
    private Button keyBtn1;
    private Button keyBtn2;
    private Button keyBtn3;
    private Button keyBtn4;
    private Button keyBtn5;
    private Button keyBtn6;
    private Button keyBtn7;
    private Button keyBtn8;
    private Button keyBtn9;
    private Button keyBtn0;
    private Button keyBtnClearAll;
    private Button keyBtnBackspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_entry);

        cardPin = "";

        cardPinTv = (TextView) findViewById(R.id.card_pin_tv);
        proceedBtn = (Button) findViewById(R.id.ok_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        keyBtn1 = (Button) findViewById(R.id.keypad_btn_1);
        keyBtn2 = (Button) findViewById(R.id.keypad_btn_2);
        keyBtn3 = (Button) findViewById(R.id.keypad_btn_3);
        keyBtn4 = (Button) findViewById(R.id.keypad_btn_4);
        keyBtn5 = (Button) findViewById(R.id.keypad_btn_5);
        keyBtn6 = (Button) findViewById(R.id.keypad_btn_6);
        keyBtn7 = (Button) findViewById(R.id.keypad_btn_7);
        keyBtn8 = (Button) findViewById(R.id.keypad_btn_8);
        keyBtn9 = (Button) findViewById(R.id.keypad_btn_9);
        keyBtn0 = (Button) findViewById(R.id.keypad_btn_0);
        keyBtnClearAll = (Button) findViewById(R.id.keypad_btn_clear_all);
        keyBtnBackspace = (Button) findViewById(R.id.keypad_btn_backspace);
    }

    public void keyPressed(View view) {
        String buttonText = ((Button) view).getText().toString();

        if (!cardPin.equals("") || !buttonText.equals("0")) {
            cardPin += buttonText;
            cardPinTv.setText(cardPinTv.getText().toString() + "*");
        }
    }

    public void deleteButtonClicked(View view) {
        String textViewText = cardPinTv.getText().toString();
        if (!textViewText.equals("")) {
            try {
                cardPinTv.setText(textViewText.substring(0, textViewText.length() - 1));
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void clearAllButtonClicked(View view) {
        try {
            cardPinTv.setText("");
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void okButtonClicked(View view) {
        Intent parent = getIntent();

        if (!cardPin.equals("")) {
            parent.putExtra("CardPin", cardPin);
            setResult(RESULT_OK, parent);
            finish();
        }
    }

    public void cancelButtonClicked(View view) {
        finish();
    }
}
