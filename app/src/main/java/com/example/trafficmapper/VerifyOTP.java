package com.example.trafficmapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class VerifyOTP extends AppCompatActivity {

    PinView pinView;


    FirebaseAuth mAuth;

    private static final String TAG = "PhoneAuth";
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificatonCallbacks;
    private PhoneAuthProvider.ForceResendingToken
            resendToken;

    private String number;
    private String codeBySystem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        pinView = findViewById(R.id.pinview);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        number = intent.getStringExtra("phoneNumber");





    }


    public void sendCode(View view) {



        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                verificatonCallbacks);

    }

    private void setUpVerificationCallbacks() {

        verificatonCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        codeBySystem = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code!=null){
                            pinView.setText(code);
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }


                    }

                    @Override
                    public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

                        if(e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(VerifyOTP.this, "invalid credentials " + e.getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }else if(e instanceof FirebaseTooManyRequestsException){
                            Toast.makeText(VerifyOTP.this, "SMS Quota exceeded", Toast.LENGTH_SHORT).show();
                        }

                    }
                };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(VerifyOTP.this, "SMS sent successfully", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();

                            startActivity(new Intent(VerifyOTP.this, MapsActivity.class));
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}