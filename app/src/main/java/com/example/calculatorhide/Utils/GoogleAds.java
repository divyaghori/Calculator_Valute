package com.example.calculatorhide.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.calculatorhide.Activity.MyApplication;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class GoogleAds {
    private static InterstitialAd mpreloadAds = null;
    static AdView mAdView = null;
    public static void loadpreloadFullAds(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, MyApplication.Ad_Intertitial, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mpreloadAds = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    }
                });
    }
    public static InterstitialAd getpreloadFullAds(Activity activity) {
        return mpreloadAds;
    }
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
    public static void bannerAdLoadGoogle(Activity context, LinearLayout linearLayout) {
        mAdView = new AdView(context);
        if (isNetworkAvailable(context)) {
            mAdView.setVisibility(View.VISIBLE);
            mAdView.setEnabled(true);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(MyApplication.Ad_Banner);
            linearLayout.setVisibility(View.VISIBLE);

            mAdView.setAdListener(new AdListener() {
                public void onAdFailedToLoad(int i) {
                    linearLayout.setVisibility(View.GONE);
                }

                public void onAdClicked() {
                    super.onAdClicked();
                }

                public void onAdLoaded() {
                    Log.i(com.google.ads.AdRequest.LOGTAG, "onAdLoaded");
                }

                public void onAdOpened() {
                    Log.i(com.google.ads.AdRequest.LOGTAG, "onAdOpened");
                }

                public void onAdLeftApplication() {
                    Log.i(com.google.ads.AdRequest.LOGTAG, "onAdLeftApplication");
                }

                public void onAdClosed() {
                    Log.i(com.google.ads.AdRequest.LOGTAG, "onAdClosed");
                }
            });

            mAdView.loadAd(new AdRequest.Builder().build());
            linearLayout.removeAllViews();
            linearLayout.addView(mAdView);

            return;
        }
        mAdView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        linearLayout.removeAllViews();
    }
}
