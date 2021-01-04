package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private final String NUMBER_EXTRA = "NUMBER_EXTRA";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";


    private TextView numberInVerification;
    private EditText editTextCode;
    private String numberFromIntent;
    private Boolean verificationInProgress = false;
    
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        numberInVerification = findViewById(R.id.textNumberVerificaion);
        editTextCode = findViewById(R.id.editTextCode);
        
        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            numberFromIntent = getIntent().getStringExtra(NUMBER_EXTRA);
            numberInVerification.setText(numberFromIntent);

            startPhoneNumberVerification(numberFromIntent);
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

            //signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    // Get the text code sent so user can use it for sign in
    private void startPhoneNumberVerification(String numberFromIntent) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(numberFromIntent)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        verificationInProgress = true;
    }

    public void actionWrongNumber(View view) {
        Intent wrongNumberIntent = new Intent(this, RegisterActivity.class);
        startActivity(wrongNumberIntent);
    }

    public void actionSubmit(View view) {

    }
}