package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.R;


public class NewPinActivity extends AppCompatActivity {

    EditText pin_et;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanepassword);
        activity = this;

        pin_et = findViewById(R.id.pin_et);
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = pin_et.getText().toString();
                if (pin.length() == 0){
                    Toast.makeText(activity,"Please Enter Answer",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(activity,"PIN is Changed successfully",Toast.LENGTH_LONG).show();
                    MyApplication.SetStringToPrefs(activity,MyApplication.PIN,pin);
                    finish();
                }
            }
        });
    }
}