package com.example.calculatorhide.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public class DisguiseActivity extends AppCompatActivity {
    private static final String disguise = "com.example.calculatorhide.icon.icon_disguise";
    ImageView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14,i15,i16,i17;
    ImageView back;
    AlertDialog.Builder builder;
    TextView maintext;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disguise);
        activity = this;
        InterstitialAd interstitialAd = GoogleAds.getpreloadFullAds(activity);
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    GoogleAds.loadpreloadFullAds(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.e("Home : ", "Error : " + adError);
                }
            });
            interstitialAd.show(activity);
        } else {
            Log.e("Home : ", "in Else part");
        }
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Disguise_Icon));
        back = findViewById(R.id.back);
        builder = new AlertDialog.Builder(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        i12 = findViewById(R.id.i12);
        i13 = findViewById(R.id.i13);
        i14 = findViewById(R.id.i14);
        i15 = findViewById(R.id.i15);
        i16 = findViewById(R.id.i16);
        i17 = findViewById(R.id.i17);


        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(0);
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(1);
            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(2);
            }
        });
        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(3);
            }
        });
        i5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(4);
            }
        });
        i6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(5);
            }
        });
        i7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(6);
            }
        });
        i8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(7);
            }
        });
        i9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(8);
            }
        });
        i10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(9);
            }
        });
        i11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(10);
            }
        });
        i12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(11);
            }
        });
        i13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(12);
            }
        });
        i14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(13);
            }
        });
        i15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(14);
            }
        });
        i16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(15);
            }
        });
        i17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(16);
            }
        });
    }
    public  void dialog(int i){
        builder.setMessage("Do you want to Chanage this app Icon?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeAppIcon(i);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Alert");
        alert.show();
    }

    public void ChangeAppIcon(int i) {
        for (int i2 = 0; i2 <= 15; i2++) {
            if (i2 == i) {
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(new ComponentName(this, disguise + i2),   PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            } else {
                PackageManager packageManager2 = getPackageManager();
                packageManager2.setComponentEnabledSetting(new ComponentName(this, disguise + i2),   PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }
}