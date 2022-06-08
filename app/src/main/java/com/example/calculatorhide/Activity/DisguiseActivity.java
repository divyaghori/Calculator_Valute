package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.calculatorhide.R;

public class DisguiseActivity extends AppCompatActivity {
    private static final String disguise = "com.example.calculatorhide.icon.icon_disguise";
    ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disguise);
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);
        i4 = findViewById(R.id.i4);
        i5 = findViewById(R.id.i5);
        i6 = findViewById(R.id.i6);
        i7 = findViewById(R.id.i7);
        i8 = findViewById(R.id.i8);
        i9 = findViewById(R.id.i9);
        i10 = findViewById(R.id.i10);
        i11 = findViewById(R.id.i11);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(0);
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(1);
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(2);
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(3);
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(4);
            }
        });
        i6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(5);
            }
        });
        i7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(6);
            }
        });
        i8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(7);
            }
        });
        i9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(8);
            }
        });
        i10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(9);
            }
        });
        i11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAppIcon(10);
            }
        });
    }
    public void ChangeAppIcon(int i) {
        for (int i2 = 0; i2 <= 11; i2++) {
            if (i2 == i) {
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(new ComponentName(this, disguise + i2),   PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            } else {
                PackageManager packageManager2 = getPackageManager();
                packageManager2.setComponentEnabledSetting(new ComponentName(this, disguise + i2),   PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }
}