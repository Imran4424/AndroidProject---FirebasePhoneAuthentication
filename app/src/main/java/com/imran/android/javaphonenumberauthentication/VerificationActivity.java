package com.imran.android.javaphonenumberauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private final String NUMBER_EXTRA = "NUMBER_EXTRA";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;


    private TextView numberInVerification;
    private EditText editTextCode;
    private String numberFromIntent;

    private Boolean verificationInProgress = false;
    private String currentVerificationId;
    private boolean verificationCompleted = false;

    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken currentForceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

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

        // Initialize phone auth callbacks
        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                verificationInProgress = false;
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                verificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "Invalid phone number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

               currentVerificationId = verificationId;
               currentForceResendingToken = forceResendingToken;
            }
        };

         if (savedInstanceState == null) {
            numberFromIntent = getIntent().getStringExtra(NUMBER_EXTRA);
            numberInVerification.setText(numberFromIntent);

            startPhoneNumberVerification(numberFromIntent);
            String code = editTextCode.getText().toString();
            if (code.isEmpty()) {
                editTextCode.setError("Can not be empty");
            } else {
                verifyPhoneNumberWithCode(currentVerificationId, code);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (verificationInProgress) {
            startPhoneNumberVerification(numberFromIntent);
        }
    }

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

    // Use text to sign in
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

//                            Intent signedInIntent = new Intent(VerificationActivity.this, SignedInActivity.class);
//                            signedInIntent.putExtra(NUMBER_EXTRA, numberFromIntent);
//                            startActivity(signedInIntent);

                            verificationCompleted = true;
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                editTextCode.setError("Invalid Code");
                            }
                        }
                    }
                });
    }

    // entered code to manually for log in (code from text)
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    // Get the text code sent so user can use it for sign in
    private void startPhoneNumberVerification(String numberFromIntent) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(numberFromIntent)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(VerificationActivity.this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        verificationInProgress = true;
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerificationActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(onVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void actionWrongNumber(View view) {
        Intent wrongNumberIntent = new Intent(this, RegisterActivity.class);
        startActivity(wrongNumberIntent);
    }

    public void actionSubmit(View view) {
        if (verificationCompleted) {
            Intent signedInIntent = new Intent(VerificationActivity.this, SignedInActivity.class);
            signedInIntent.putExtra(NUMBER_EXTRA, numberFromIntent);
            startActivity(signedInIntent);
        }
    }
}