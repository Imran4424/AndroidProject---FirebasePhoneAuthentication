package com.imran.android.javaphonenumberauthentication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Spinner spinnerCountry;
    private EditText editTextCountryCode;
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinnerCountry = findViewById(R.id.spinnerCountry);
    }

    public void actionNext(View view) {

    }
}