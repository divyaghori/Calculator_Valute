package com.example.calculatorhide.Utils;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;


public class AppOpenManager  {
    private static final String LOG_TAG = "AppOpenManager";
//    private static final String AD_UNIT_ID = "ca-app-pub-68959573532223023/3473201416";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static boolean isShowingAd = false;
    private final Application myApplication;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    public AppOpenManager(Application myApplication) {
        this.myApplication = myApplication;

    }

    public void fetchAd(Context context) {
        if (isAdAvailable()) {
            return;
        }

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        AppOpenManager.this.appOpenAd = appOpenAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d(LOG_TAG, "error in loading ad open " + loadAdError.getMessage());
                        Log.d(LOG_TAG, "error in loading ad open " + loadAdError.getResponseInfo());
                    }
                };
        SessionManager sessionManager = new SessionManager(context);
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, sessionManager.getOpenAppId(), request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    public AppOpenAd showAdIfAvailable(Context context) {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");
            return appOpenAd;
        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd(context);
            return null;
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
    }
}