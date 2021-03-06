package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String NUMBER_EXTRA = "NUMBER_EXTRA";
    private Spinner spinnerCountry;
    private EditText editTextCountryCode;
    private EditText editTextNumber;
    private List<String> countryCodesList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinnerCountry = findViewById(R.id.spinnerCountry);
        // this is really important
        spinnerCountry.setOnItemSelectedListener(this);

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextNumber = findViewById(R.id.editTextNumberRegister);

        countryCodesList = Arrays.asList(getResources().getStringArray(R.array.country_code));

        //   the following code works too
//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.country_names,
//                android.R.layout.simple_spinner_item
//        );
//
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCountry.setAdapter(spinnerAdapter);
    }

    private boolean validatePhoneNumber() {
        String enteredNumber = editTextNumber.getText().toString();

        if (enteredNumber.isEmpty()) {
            editTextNumber.setError("Invalid Phone Number");
            return false;
        }

        return true;
    }

    public void actionNext(View view) {
        if (!validatePhoneNumber()) {
            return;
        }

        String phoneNumber = editTextCountryCode.getText().toString() + editTextNumber.getText().toString();

        Intent verificationIntent = new Intent(this, VerificationActivity.class);
        verificationIntent.putExtra(NUMBER_EXTRA, phoneNumber);
        startActivity(verificationIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editTextCountryCode.setText(countryCodesList.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}