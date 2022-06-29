package com.example.calculatorhide.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAdManager {
    private InterstitialAd mInterstitialAd = null;

    public void fetchAd(Context context, Boolean isCal) {
        if (isCal) {
            SessionManager sessionManager = new SessionManager(context);
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(context, sessionManager.getCalAdId(), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i("TAG", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("TAG", loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
        } else {
            SessionManager sessionManager = new SessionManager(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, sessionManager.getHomeAdId(), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i("TAG", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("TAG", loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
        }

    }

    public InterstitialAd showIfItAvaible() {
        return mInterstitialAd;
    }
}
