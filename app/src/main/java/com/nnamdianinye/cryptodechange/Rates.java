package com.nnamdianinye.cryptodechange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rates extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView errorMessage;
    private ProgressBar progressBar;
    Spinner spinner;
    ArrayAdapter<CharSequence> spinnerAdapter;
    List<Data> myDataList = new ArrayList<>();
    TrueOrFalse trueOrFalse;
    private static final String TAG = "Rates";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorMessage = (TextView) findViewById(R.id.error_message);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(Rates.this, R.array.currency_list,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(Rates.this);

        Bundle data = getIntent().getExtras();
        String title = data.getString("title");
        if (title.contentEquals("BTC Rates")) {
            ActionBar actionBar = getSupportActionBar();
            trueOrFalse = new TrueOrFalse(true, false);
            try {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setLogo(R.drawable.bitcoin);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setTitle(R.string.btc_rates);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else if (title.contentEquals("ETH Rates")) {
            ActionBar actionBar = getSupportActionBar();
            trueOrFalse = new TrueOrFalse(false, true);
            try {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setLogo(R.drawable.eth);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setTitle(R.string.eth_rates);
                actionBar.setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "onCreate: " + trueOrFalse.isEth());
        Log.d(TAG, "onCreate: " + trueOrFalse.isBtc());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(Rates.this, myDataList, trueOrFalse, new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String myUrl = null;
        String baseCurrency = parent.getSelectedItem().toString();
        if (trueOrFalse.isBtc()) {
            myUrl = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=" +
                    baseCurrency;
        } else if (trueOrFalse.isEth()) {
            myUrl = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=" +
                    baseCurrency;
        }
        requestData(myUrl);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                Intent intent = new Intent(Rates.this, Key.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void setProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }


    private void requestData(String url) {
        setProgressBar();
        errorMessage.setVisibility(TextView.GONE);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String value = null;
                String key;
                Iterator<String> iterator = response.keys();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    try {
                        value = response.getString(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Data data = new Data(key, value);
                    myDataList.add(0, data);
                }
                progressBar.setVisibility(ProgressBar.GONE);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onResponse: " + myDataList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(Rates.this, "Request Error", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(ProgressBar.GONE);
                errorMessage.setVisibility(TextView.VISIBLE);
            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}