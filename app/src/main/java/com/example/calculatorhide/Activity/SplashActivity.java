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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.LOCALIZATION.LocaleHelper;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.PreferenceManager;
import com.example.calculatorhide.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private Activity activity = this;
    private ActivitySplashBinding binding;
    Context context;
    public static Resources resources;
    Boolean getdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();

        //  MyApplication.appOpenAdManager = new MyApplication.AppOpenAdManager();

        //appOpenAdManager.showAdIfAvailable(currentActivity);
//        MyApplication app = (MyApplication) getApplication();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                app.ShowOpenAd(SplashActivity.this,new MyApplication.OnShowAdCompleteListener() {
//                    @Override
//                    public void onShowAdComplete() {
//                        startMainActivity();
//                    }
//                });
//            }
//        }, 2000);

        MyApplication app = (MyApplication) getApplication();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                app.ShowOpenAd();
                GoogleAds.loadpreloadFullAds(SplashActivity.this);
            }
        }, 500);
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
        }, 5000);
    }

    public void startMainActivity() {
        getdata = PreferenceManager.getInstance(getApplicationContext()).getpreferenceboolean("login");
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if (getdata == false) {
                    Intent mainIntent1 = new Intent(SplashActivity.this,GuideActivity.class);
                    startActivity(mainIntent1);
                }
                else {
                    Intent mainIntent = new Intent(SplashActivity.this,CalculatorActivity.class);
                    startActivity(mainIntent);

                }
            }
        }, 2000);
    }
}