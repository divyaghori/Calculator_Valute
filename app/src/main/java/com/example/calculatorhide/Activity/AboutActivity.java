package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.calculatorhide.R;

public class AboutActivity extends AppCompatActivity {

    TextView maintext,aboutmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.About_Us));
        aboutmsg = findViewById(R.id.aboutmsg);
        aboutmsg.setText(SplashActivity.resources.getString(R.string.AboutUsMsg));
    }
}