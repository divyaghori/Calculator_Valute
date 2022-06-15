package com.example.calculatorhide.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    private static PreferenceManager instance;
    private static SharedPreferences settings;

    public static PreferenceManager getInstance(Context context)
    {

        if(instance==null)
        {
            instance=new PreferenceManager(context);
        }
        return instance;
    }


    private static Context ctx;
    private String json;

    public PreferenceManager(Context context) {
        ctx = context;
    }
    public void clearPrefs(Context context) {
        settings = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }


    public void setPass(Context context,String password)
    {
        SharedPreferences.Editor editor=context.getSharedPreferences(context.getPackageName(),context.MODE_PRIVATE).edit();
        editor.putString("password",password);
        editor.commit();
    }
    public String getPass(Context context)
    {
        settings = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        SharedPreferences prefs=context.getSharedPreferences(context.getPackageName(),context.MODE_PRIVATE);
        return prefs.getString("password","");

    }

    public void setPreferenceBolean(Boolean login, String key) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(key, login);
        prefsEditor.commit();
    }

    public Boolean getpreferenceboolean(String key) {
        SharedPreferences appSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(
                ctx.getApplicationContext());
        Boolean json = appSharedPrefs.getBoolean(key, false);
        return json;
    }




}
