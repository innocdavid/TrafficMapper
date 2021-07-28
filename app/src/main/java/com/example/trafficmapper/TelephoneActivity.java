package com.example.trafficmapper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TelephoneActivity extends AppCompatActivity {




    private CountryCodePicker countryCodePicker;
    private EditText phoneNumber;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    String number;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone);


        phoneNumber = findViewById(R.id.editextOne);

        countryCodePicker = findViewById(R.id.countryCodePicker);
        countryCodePicker.registerCarrierNumberEditText(phoneNumber);





    }


    public void CallVerifyOTPScreen(View view) {

        String val = phoneNumber.getText().toString();
        number = countryCodePicker.getFullNumberWithPlus();

        if(val.isEmpty()){
            phoneNumber.setError("field empty");
            phoneNumber.requestFocus();
        } else if (val.length()!= 9){
            phoneNumber.setError("invalid number");
        }else{
            Intent intent = new Intent(TelephoneActivity.this, VerifyOTP.class);
            intent.putExtra("phoneNumber", number);
            startActivity(intent);
        }
    }
}