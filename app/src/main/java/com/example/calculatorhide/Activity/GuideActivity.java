package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.PreferenceManager;

public class GuideActivity extends AppCompatActivity {
    LinearLayout selectBtn,privacy,term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        selectBtn = findViewById(R.id.selectBtn);
        privacy = findViewById(R.id.privacy);
        term = findViewById(R.id.terms);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://calculatorvaulthelp.blogspot.com/2021/02/privacy-policy-body-font-family.html"));
                startActivity(browserIntent);
            }
        });
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://calculatorvaulthelp.blogspot.com/2021/02/privacy-policy-body-font-family.html"));
                startActivity(browserIntent);
            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getInstance(getApplicationContext()).setPreferenceBolean(true,"login");
                finish();
                Intent i = new Intent(GuideActivity.this,CalculatorActivity.class);
                startActivity(i);
            }
        });
    }
}