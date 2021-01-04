package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignedInActivity extends AppCompatActivity {
    private final String NUMBER_EXTRA = "NUMBER_EXTRA";

    private TextView textNumber;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        textNumber = findViewById(R.id.textNumberSigned);
        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            String numberFromIntent = getIntent().getStringExtra(NUMBER_EXTRA);
            textNumber.setText(numberFromIntent);
        }
    }

    public void actionSignOut(View view) {
        firebaseAuth.signOut();
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
    }
}