/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.calculatorhide.Utils.AppOpenManager;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.FirebaseApp;

import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application implements ActivityLifecycleCallbacks, LifecycleObserver {

//    public AppOpenAdManager appOpenAdManager;
//    private Activity currentActivity;

    public static String Ad_Open = "ca-app-pub-3940256099942544/3419835294";
    public static String Ad_Intertitial = "ca-app-pub-3940256099942544/1033173712";
    public static String Ad_Banner = "ca-app-pub-3940256099942544/6300978111";
    private AppOpenManager appOpenManager;
    private InterstitialAdManager interstitialAdManager;
    public static String PIN = "pin";
    public static String QUESTION = "Question";
    public static String ANSWER = "Answer";
//    private boolean isOpanAdLoaded; // false
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        this.registerActivityLifecycleCallbacks(this);
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                    }
                });
        List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        appOpenManager = new AppOpenManager(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//        appOpenAdManager = new AppOpenAdManager();
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        ActivityLifecycleCallbacks.super.onActivityPreCreated(activity, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }


    private String TAG = "MyApp";
    // load

    public void LoadOpenAds(@NonNull Activity activity) {
        appOpenManager = new AppOpenManager(this);
        appOpenManager.fetchAd(this);
        Log.d(TAG, "Load Open Ads: ");
    }


    // show
    //   @Override
    public void ShowOpenAd(@NonNull Activity activity) {
//        if (isOpanAdLoaded) return;
//        isOpanAdLoaded = true;
        AppOpenAd appOpenAd = appOpenManager.showAdIfAvailable(this);
        if (appOpenAd != null) {
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Intent ab = new Intent(activity, CalculatorActivityy.class);
                    startActivity(ab);
                }
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Intent ab = new Intent(activity, CalculatorActivityy.class);
                    startActivity(ab);
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(activity);
        } else {
            Intent ab = new Intent(activity, CalculatorActivityy.class);
            startActivity(ab);
        }
        Log.d(TAG, "onActivityResumed: ");
        ///////////////// Interstitial ///////////////
//        if (interstitialAdManager != null) {
//            if (interstitialAdManager.showIfItAvaible() != null) {
//                InterstitialAd adLoader = interstitialAdManager.showIfItAvaible();
//                if (adLoader != null) {
//                    adLoader.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            super.onAdDismissedFullScreenContent();
//                            Intent intent = new Intent(MyApplication.this, CalculatorActivityy.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                            super.onAdFailedToShowFullScreenContent(adError);
//                            Intent intent = new Intent(MyApplication.this, CalculatorActivityy.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                    });
//                    adLoader.show(activity);
//                } else {
//                    Intent intent = new Intent(this, CalculatorActivityy.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    private boolean isShowAds;
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
        // appOpenAd.showAdIfAvailable(currentActivity);
        Log.e(TAG,"Run in Foreground");
    }
    @Override
    public void onActivityResumed(@NonNull Activity activity) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!isShowAds)
//                    ShowOpenAd(activity);
//                return;
//            }
//        }, 2000);

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        isShowAds = false;
        Log.d(TAG, "onActivityPaused: ");
        //isOpanAdLoaded = false;
//        interstitialAdManager = new InterstitialAdManager();
//        interstitialAdManager.fetchAd(this,false);
        LoadOpenAds(activity);
//        appOpenManager = new AppOpenManager(this);
//        appOpenManager.fetchAd(this);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    //    public void ShowOpenAd() {
//        appOpenAdManager.showAdIfAvailable(currentActivity);
//
//    }
//
//    public void ShowOpenAd(Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
//
//    }
//    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
////  @OnLifecycleEvent(Event.ON_START)
////  protected void onMoveToForeground() {
////    // Show the ad (if available) when the app moves to foreground.
////    appOpenAdManager.showAdIfAvailable(currentActivity);
////  }
//
//    /**
//     * ActivityLifecycleCallback methods.
//     */
//    @Override
//    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//    }
//
//    @Override
//    public void onActivityStarted(@NonNull Activity activity) {
//        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
//        // SDK or another activity class implemented by a third party mediation partner. Updating the
//        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
//        // one that shows the ad.
//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity;
//        }
//    }
//
//    @Override
//    public void onActivityResumed(@NonNull Activity activity) {
//    }
//
//    @Override
//    public void onActivityPaused(@NonNull Activity activity) {
//    }
//
//    @Override
//    public void onActivityStopped(@NonNull Activity activity) {
//    }
//
//    @Override
//    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//    }
//
//    @Override
//    public void onActivityDestroyed(@NonNull Activity activity) {
//    }
//
//    /**
//     * Shows an app open ad.
//     *
//     * @param activity                 the activity that shows the app open ad
//     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
//     */
//    public void showAdIfAvailable(
//            @NonNull Activity activity,
//            @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
//        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
//        // class.
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
//    }
//
//    /**
//     * Interface definition for a callback to be invoked when an app open ad is complete
//     * (i.e. dismissed or fails to show).
//     */
//    public interface OnShowAdCompleteListener {
//        void onShowAdComplete();
//    }
//
//    /**
//     * Inner class that loads and shows app open ads.
//     */
//    static class AppOpenAdManager {
//
//        private static final String LOG_TAG = "AppOpenAdManager";
//        private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
//
//        private AppOpenAd appOpenAd = null;
//        private boolean isLoadingAd = false;
//        private boolean isShowingAd = false;
//
//        /**
//         * Keep track of the time an app open ad is loaded to ensure you don't show an expired ad.
//         */
//        private long loadTime = 0;
//
//        /**
//         * Constructor.
//         */
//        public AppOpenAdManager() {
//        }
//
//        /**
//         * Load an ad.
//         *
//         * @param context the context of the activity that loads the ad
//         */
//        private void loadAd(Context context) {
//            // Do not load ad if there is an unused ad or one is already loading.
//            if (isLoadingAd || isAdAvailable()) {
//                return;
//            }
//
//            isLoadingAd = true;
//            AdRequest request = new AdRequest.Builder().build();
//            AppOpenAd.load(
//                    context,
//                    AD_UNIT_ID,
//                    request,
//                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//                    new AppOpenAdLoadCallback() {
//                        /**
//                         * Called when an app open ad has loaded.
//                         *
//                         * @param ad the loaded app open ad.
//                         */
//                        @Override
//                        public void onAdLoaded(AppOpenAd ad) {
//                            appOpenAd = ad;
//                            isLoadingAd = false;
//                            loadTime = (new Date()).getTime();
//
//                            Log.d(LOG_TAG, "onAdLoaded.");
//                        }
//
//                        /**
//                         * Called when an app open ad has failed to load.
//                         *
//                         * @param loadAdError the error.
//                         */
//                        @Override
//                        public void onAdFailedToLoad(LoadAdError loadAdError) {
//                            isLoadingAd = false;
//                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
//                        }
//                    });
//        }
//
//        /**
//         * Check if ad was loaded more than n hours ago.
//         */
//        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
//            long dateDifference = (new Date()).getTime() - loadTime;
//            long numMilliSecondsPerHour = 3600000;
//            return (dateDifference < (numMilliSecondsPerHour * numHours));
//        }
//
//        /**
//         * Check if ad exists and can be shown.
//         */
//        private boolean isAdAvailable() {
//            // Ad references in the app open beta will time out after four hours, but this time limit
//            // may change in future beta versions. For details, see:
//            // https://support.google.com/admob/answer/9341964?hl=en
//            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
//        }
//
//        /**
//         * Show the ad if one isn't already showing.
//         *
//         * @param activity the activity that shows the app open ad
//         */
//        private void showAdIfAvailable(@NonNull final Activity activity) {
//            showAdIfAvailable(
//                    activity,
//                    new OnShowAdCompleteListener() {
//                        @Override
//                        public void onShowAdComplete() {
//                            // Empty because the user will go back to the activity that shows the ad.
//                        }
//                    });
//        }
//
//        /**
//         * Show the ad if one isn't already showing.
//         *
//         * @param activity                 the activity that shows the app open ad
//         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
//         */
//        private void showAdIfAvailable(
//                @NonNull final Activity activity,
//                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
//            // If the app open ad is already showing, do not show the ad again.
//            if (isShowingAd) {
//                Log.d(LOG_TAG, "The app open ad is already showing.");
//                return;
//            }
//
//            // If the app open ad is not available yet, invoke the callback then load the ad.
//            if (!isAdAvailable()) {
//                Log.d(LOG_TAG, "The app open ad is not ready yet.");
//                onShowAdCompleteListener.onShowAdComplete();
//                loadAd(activity);
//                return;
//            }
//
//            Log.d(LOG_TAG, "Will show ad.");
//
//            appOpenAd.setFullScreenContentCallback(
//                    new FullScreenContentCallback() {
//                        /** Called when full screen content is dismissed. */
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Set the reference to null so isAdAvailable() returns false.
//                            appOpenAd = null;
//                            isShowingAd = false;
//
//                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");
//
//                            onShowAdCompleteListener.onShowAdComplete();
//                            loadAd(activity);
//                        }
//
//                        /** Called when fullscreen content failed to show. */
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            appOpenAd = null;
//                            isShowingAd = false;
//
//                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
//
//                            onShowAdCompleteListener.onShowAdComplete();
//                            loadAd(activity);
//                        }
//
//                        /** Called when fullscreen content is shown. */
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
//                        }
//                    });
//
//            isShowingAd = true;
//            appOpenAd.show(activity);
//        }
//    }
    public static void SetIntToPrefs(Activity activity, String key, int value) {
        SharedPreferences prefs = activity.getPreferences(activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int GetIntFromPrefs(Activity activity, String key) {
        SharedPreferences prefs = activity.getPreferences(activity.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static void SetStringToPrefs(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply(); // or editor.commit()

    }

    public static String GetStringFromPrefs(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void SetBooleanToPrefs(Activity activity, String key, boolean value) {
        SharedPreferences prefs = activity.getPreferences(activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean GetBooleanFromPrefs(Activity activity, String key) {
        SharedPreferences prefs = activity.getPreferences(activity.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static boolean CheckPrefs(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(key);
    }
}
