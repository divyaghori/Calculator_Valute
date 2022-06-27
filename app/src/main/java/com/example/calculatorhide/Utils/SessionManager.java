package com.example.calculatorhide.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    public static String openAppId = null;
    public static String openCalAdId = null;
    public static String openHomeAdId = null;
    private final SharedPreferences.Editor myEdit;
    private final SharedPreferences sharedPreferences;
    Context context;

    public SessionManager(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("cal", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }

    public String getOpenAppId() {
        return sharedPreferences.getString(openAppId, "ca-app-pub-3940256099942544/3419835294");
    }

    public void setOpenAppId(String openApp) {
        myEdit.putString(openAppId, openApp);
        myEdit.commit();
    }

    public String getCalAdId() {
        return sharedPreferences.getString(openCalAdId, "ca-app-pub-3940256099942544/1033173712");
    }

    public void setCanAdId(String openCalAd) {
        myEdit.putString(openCalAdId, openCalAd);
        myEdit.commit();
    }

    public String getHomeAdId() {
        return sharedPreferences.getString(openHomeAdId, "ca-app-pub-3940256099942544/1033173712");
    }

    public void setHomeAdId(String openHomeAd) {
        myEdit.putString(openHomeAdId, openHomeAd);
        myEdit.commit();
    }


}
