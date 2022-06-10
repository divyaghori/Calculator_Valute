package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.R;
public class DisguiseActvity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disguise_actvity);
    }
    public void onMoonClick(View view) {
        String moon = (String)view.getTag();
        findViewById(R.id.moon1).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.moon2).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.moon3).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.moon4).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.moon5).setBackgroundColor(Color.TRANSPARENT);
        for (int i = 1; i < 6; i++) {
            this.getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.calculatorhide.Moon" + i),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
        view.setBackgroundColor(Color.GREEN);
        this.getPackageManager().setComponentEnabledSetting(
                new ComponentName(BuildConfig.APPLICATION_ID, "com.example.calculatorhide." + moon),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}