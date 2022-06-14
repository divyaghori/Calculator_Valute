package com.example.calculatorhide.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class DocumentActivity extends AppCompatActivity {
    ImageView icback,getimage;
    TextView maintext,filenotfound;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        findId();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    private void findId() {
        icback = findViewById(R.id.back);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getimage = findViewById(R.id.getimage);
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DocumentActivity.this,ImportDocument.class);
                startActivity(i);
            }
        });
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Documents));
        filenotfound = findViewById(R.id.filenotfound);
        filenotfound.setText(SplashActivity.resources.getString(R.string.No_files_added));
    }
}