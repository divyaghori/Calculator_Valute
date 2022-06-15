package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.PreferenceManager;

public class GuideActivity extends AppCompatActivity {
    LinearLayout selectBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        selectBtn = findViewById(R.id.selectBtn);
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