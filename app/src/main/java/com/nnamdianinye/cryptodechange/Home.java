package com.nnamdianinye.cryptodechange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity implements View.OnClickListener {

    TextView btc, eth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btc = (TextView) findViewById(R.id.tvBTC);
        eth = (TextView) findViewById(R.id.tvETH);

        btc.setOnClickListener(this);
        eth.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBTC:
                Bundle bundle = new Bundle();
                bundle.putString("title", "BTC Rates");
                Intent intent = new Intent(Home.this, Rates.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tvETH:
                Bundle ethBundle = new Bundle();
                ethBundle.putString("title", "ETH Rates");
                Intent ethIntent = new Intent(Home.this, Rates.class);
                ethIntent.putExtras(ethBundle);
                startActivity(ethIntent);
                break;
        }
    }
}
