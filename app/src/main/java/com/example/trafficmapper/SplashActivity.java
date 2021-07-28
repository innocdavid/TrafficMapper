package com.example.trafficmapper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {


    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //TODO: code for the splash screen.......
        handler = new Handler();
        handler.postDelayed(() -> {

            Intent intent = new Intent(SplashActivity.this, TelephoneActivity.class);
            startActivity(intent);
            finish();
        }, 5000);

    }
}