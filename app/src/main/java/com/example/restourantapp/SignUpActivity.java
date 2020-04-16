package com.example.restourantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restourantapp.view.CountryAdapter;
import com.example.restourantapp.view.CountrySpinner;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private ArrayList<CountrySpinner> mCountryList;
    private CountryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initList();
        Spinner spinnerCountries = findViewById(R.id.spinner);
        mAdapter = new CountryAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountrySpinner selectedItem = (CountrySpinner) parent.getItemAtPosition(position);
                String selectedCountryCode = selectedItem.getCountryCode();
                Toast.makeText(SignUpActivity.this, selectedCountryCode, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountrySpinner("+386", R.drawable.flag_serbia));
        mCountryList.add(new CountrySpinner("+383", R.drawable.flag_kosovo));
    }


}
