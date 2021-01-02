package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
    }

    public void actionWrongNumber(View view) {
        Intent wrongNumberIntent = new Intent(this, RegisterActivity.class);
        startActivity(wrongNumberIntent);
    }

    public void actionSubmit(View view) {

    }
}