package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {
    private Activity activity=this;
    private ActivitySplashBinding binding;
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
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        binding.tvVersion.setText("Version "+ BuildConfig.VERSION_NAME);
//        binding.lavLoad.setProgress(1);
//        binding.lavLoad.pauseAnimation();
        binding.lavLoad.loop(true);
        binding.lavLoad.playAnimation();
//        try
//        {
//            Drawable icon = getPackageManager().getApplicationIcon("com.example.calculatorhide");
//            binding.ivIcon.setImageDrawable(icon);
//        }
//        catch ( PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }


//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//              startActivity(new Intent(activity, CalculatorActivity.class));
//              finish();
//            }
//        }, 3000);
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