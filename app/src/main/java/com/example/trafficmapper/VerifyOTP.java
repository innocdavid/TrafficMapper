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

   private String codeBySystem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        pinView = findViewById(R.id.pinview);

        mAuth = FirebaseAuth.getInstance();

       String _phoneNo = getIntent().getStringExtra("phoneNumber");

        sendVerificationCodeToUser(_phoneNo);

    }


    public void sendVerificationCodeToUser(String phoneNumber) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }

   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
           = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

       @Override
       public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
           super.onCodeSent(s, forceResendingToken);
           codeBySystem = s;
       }

       @Override
       public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                pinView.setText(code);
                verifyCode(code);
            }
       }

       @Override
       public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

           Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();

       }
   };

    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(VerifyOTP.this, "verification complete", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerifyOTP.this, GoogleActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyOTP.this, "verification failed, try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    public void CallNextVerifyOTPScreen(View view) {

        String code = pinView.getText().toString();

        if(!code.isEmpty()){

            verifyCode(code);
        }
    }

}