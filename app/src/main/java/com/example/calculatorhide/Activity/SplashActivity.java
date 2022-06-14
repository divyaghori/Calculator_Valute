package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.LOCALIZATION.LocaleHelper;
import com.example.calculatorhide.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private Activity activity = this;
    private ActivitySplashBinding binding;
    Context context;
    public static Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        binding.tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
        binding.lavLoad.loop(true);
        binding.lavLoad.playAnimation();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Application application = getApplication();
                if (!(application instanceof MyApplication)) {
                    startMainActivity();
                    return;
                }
                ((MyApplication) application)
                        .showAdIfAvailable(
                                SplashActivity.this,
                                new MyApplication.OnShowAdCompleteListener() {
                                    @Override
                                    public void onShowAdComplete() {
                                        startMainActivity();
                                    }

                                });

            }
        }, 2000);
    }
    public void startMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }
}