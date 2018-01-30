package ng.novateur.novapay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.paystack.android.PaystackSdk;

public class AmountEntryActivity extends AppCompatActivity {

    private static final String TAG = Class.class.getSimpleName();

    private TextView amountTv;
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
        setContentView(R.layout.activity_amount_entry);
        PaystackSdk.initialize(getApplicationContext());

        amountTv = (TextView) findViewById(R.id.amount_tv);
        proceedBtn = (Button) findViewById(R.id.proceed_btn);
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
        String amountTvText = amountTv.getText().toString();
        String buttonText = ((Button) view).getText().toString();

        if (amountTvText.equals("") && buttonText.equals("0")) {
            return;
        } else {
            amountTv.setText(amountTvText + buttonText);
        }
    }

    public void deleteButtonClicked(View view) {
        String textViewText = amountTv.getText().toString();
        if (textViewText.equals("")) {
        } else {
            try {
                amountTv.setText(textViewText.substring(0, textViewText.length() - 1));
            } catch (IndexOutOfBoundsException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    public void clearAllButtonClicked(View view) {
        try {
            amountTv.setText("");
        } catch (NullPointerException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void proceedButtonClicked(View view) {
        Intent parent = getIntent();
        int amount;

        try {
            if (!amountTv.getText().toString().equals("")) {
                amount = Integer.parseInt(amountTv.getText().toString());

                parent.putExtra("Amount", amount);
                setResult(RESULT_OK, parent);
                finish();
            }
        } catch (Exception e) {
            Toast.makeText( AmountEntryActivity.this, e.getCause() + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void cancelButtonClicked(View view) {
        finish();
    }
}
