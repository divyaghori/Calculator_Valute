package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.calculatorhide.R;

public class CalcualatorTransitionActivity extends AppCompatActivity {

    int index;
    CardView cardview;
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_activity_transtion);
        cardview = findViewById(R.id.cardview);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        index = getIntent().getIntExtra("index",0);
        if(index == 0){
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(CalcualatorTransitionActivity.this,HomeActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }
    }
}
