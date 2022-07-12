package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.R;


public class NewPinActivity extends AppCompatActivity {

    EditText pin_et;
    Activity activity;
    TextView maintext,enter,submit_btn;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanepassword);
        activity = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pin_et = findViewById(R.id.pin_et);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Change_password));
        enter = findViewById(R.id.enter);
        enter.setText(SplashActivity.resources.getString(R.string.Enter_your_Password));
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setText(SplashActivity.resources.getString(R.string.Confirm));
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = pin_et.getText().toString();
                if (pin.length() != 4){
                    Toast.makeText(activity,"Please Enter 4 Digit PIN",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(activity,"PIN is Changed successfully",Toast.LENGTH_LONG).show();
                    MyApplication.SetStringToPrefs(activity,MyApplication.PIN,pin);
                    finish();
                }
            }
        });
    }
}