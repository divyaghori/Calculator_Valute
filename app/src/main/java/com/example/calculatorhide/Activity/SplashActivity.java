package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.example.calculatorhide.LOCALIZATION.LocaleHelper;
import com.example.calculatorhide.R;

public class SplashActivity extends AppCompatActivity {

    Context context;
    public static Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (LocaleHelper.getLanguage(SplashActivity.this).equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(SplashActivity.this, "en");
            resources = context.getResources();
        } else if (LocaleHelper.getLanguage(SplashActivity.this).equalsIgnoreCase("es")) {
            context = LocaleHelper.setLocale(SplashActivity.this, "es");
            resources = context.getResources();
        } else {
            context = LocaleHelper.setLocale(SplashActivity.this, "en");
            resources = context.getResources();
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                    Intent mainIntent1 = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(mainIntent1);
            }
        }, 2000);
    }
}