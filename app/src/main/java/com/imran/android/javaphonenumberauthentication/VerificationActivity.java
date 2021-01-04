package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {
    private final String NUMBER_EXTRA = "NUMBER_EXTRA";
    private TextView numberInVerification;
    private EditText editTextCode;
    private String numberFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        numberInVerification = findViewById(R.id.textNumberVerificaion);
        editTextCode = findViewById(R.id.editTextCode);

//        if (savedInstanceState == null) {
//            numberFromIntent = getIntent().getStringExtra(NUMBER_EXTRA);
//            numberInVerification.setText(numberFromIntent);
//        }
    }

    public void actionWrongNumber(View view) {
        Intent wrongNumberIntent = new Intent(this, RegisterActivity.class);
        startActivity(wrongNumberIntent);
    }

    public void actionSubmit(View view) {

    }
}