package com.example.calculatorhide.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    private static PreferenceManager instance;
    private static SharedPreferences settings;

    public static PreferenceManager getInstance()
    {

        if(instance==null)
        {
            instance=new PreferenceManager();
        }
        return instance;
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


}
