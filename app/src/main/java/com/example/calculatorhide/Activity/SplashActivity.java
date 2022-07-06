package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.LOCALIZATION.LocaleHelper;
import com.example.calculatorhide.Utils.ActivityData;
import com.example.calculatorhide.Utils.AdDetails;
import com.example.calculatorhide.Utils.AppOpenManager;
import com.example.calculatorhide.Utils.PreferenceManager;
import com.example.calculatorhide.Utils.SessionManager;
import com.example.calculatorhide.Utils.Util;
import com.example.calculatorhide.databinding.ActivitySplashBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class SplashActivity extends AppCompatActivity {
    public static Resources resources;
    Context context;
    Boolean getdata;
    DatabaseReference reference;
    private Activity activity = this;
    private ActivitySplashBinding binding;
    private AppOpenManager appOpenManager;
    private SessionManager sessionManager;

   // private int CurrentScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);
        firebaseData();
        initUi();
        Log.d("Splash a", "onCreate: ");
      //  CurrentScreen = 0;
       // if (CurrentScreen > Util.CurrentScreen){   Util.CurrentScreen = CurrentScreen;}
       // Util.activityData_list.add(new ActivityData(SplashActivity.this,true));
    }

    private void firebaseData() {
        reference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdDetails details = dataSnapshot.getValue(AdDetails.class);
                Log.e("Ad cal ",details.getCalAdId().toString());
                Log.e("Ad open",details.getOpenAppId().toString());
                Log.e("Ad home",details.getHomeAdId().toString());
                sessionManager.setCanAdId(details.getCalAdId());
                sessionManager.setOpenAppId(details.getOpenAppId());
                sessionManager.setHomeAdId(details.getHomeAdId());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addListenerForSingleValueEvent(valueEventListener);
    }

    private void initUi() {
        appOpenManager = new AppOpenManager(getApplication());
        appOpenManager.fetchAd(SplashActivity.this);
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
                startMainActivity();
                return;
            }
        }, 2000);
    }

    public void startMainActivity() {
        getdata = PreferenceManager.getInstance(getApplicationContext()).getpreferenceboolean("login");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               AppOpenAd appOpenAd = appOpenManager.showAdIfAvailable(SplashActivity.this);
               if (appOpenAd != null) {
                   FullScreenContentCallback fullScreenContentCallback =
                           new FullScreenContentCallback() {
                               @Override
                               public void onAdDismissedFullScreenContent() {
                                   if (getdata == false) {
                                       Intent mainIntent1 = new Intent(SplashActivity.this, GuideActivity.class);
                                       startActivity(mainIntent1);
                                   } else {
                                       Intent mainIntent = new Intent(SplashActivity.this, CalculatorActivityy.class);
                                       startActivity(mainIntent);
                                   }
                               }
                               @Override
                               public void onAdFailedToShowFullScreenContent(AdError adError) {
                                   if (getdata == false) {
                                       Intent mainIntent1 = new Intent(SplashActivity.this, GuideActivity.class);
                                       startActivity(mainIntent1);
                                   } else {
                                       Intent mainIntent = new Intent(SplashActivity.this, CalculatorActivityy.class);
                                       startActivity(mainIntent);
                                   }
                               }
                               @Override
                               public void onAdShowedFullScreenContent() {
                               }
                           };

                   appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                   appOpenAd.show(SplashActivity.this);
               } else {
                    if (getdata == false) {
                        Intent mainIntent1 = new Intent(SplashActivity.this, GuideActivity.class);
                        startActivity(mainIntent1);
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, CalculatorActivityy.class);
                        startActivity(mainIntent);

                    }
                }

            }
        }, 5000);
    }
}