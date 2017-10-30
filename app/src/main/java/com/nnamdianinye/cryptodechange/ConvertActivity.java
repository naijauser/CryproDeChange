package com.nnamdianinye.cryptodechange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ConvertActivity extends AppCompatActivity {
    TextView crypto, cryptoValue, base;
    EditText baseValue;
    DecimalFormat decimalFormat;
    double rate;
    private static final String TAG = "ConvertActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        decimalFormat = new DecimalFormat("0.0000000000");

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        crypto = (TextView) findViewById(R.id.crypto_converter);
        cryptoValue = (TextView) findViewById(R.id.crypto_converter_value);
        base = (TextView) findViewById(R.id.base_converter);
        baseValue = (EditText) findViewById(R.id.base_converter_value);

        Bundle data = getIntent().getExtras();
        Log.d(TAG, "onCreate: crypto :" + data.getString("crypto") +
                " cryptoValue :" + data.getString("cryptoValue") +
                " base :" + data.getString("base") +
                " baseValue :" + data.getString("baseValue"));
        String string = data.getString("baseValue");
        rate = Double.parseDouble(string);

        crypto.setText(data.getString("crypto"));
        cryptoValue.setText(data.getString("cryptoValue"));
        base.setText(data.getString("base"));
        baseValue.setText(Double.toString(rate));

        baseValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (baseValue.getText().toString().equals("")) {
                    cryptoValue.setText(R.string.zero);
                } else if (!baseValue.getText().toString().equals("")) {
                    try {
                        double value = Double.parseDouble(s.toString());
                        double conversion = (1 / rate) * value;
                        cryptoValue.setText(decimalFormat.format(conversion));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_key:
                Intent intent = new Intent(ConvertActivity.this, Key.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
