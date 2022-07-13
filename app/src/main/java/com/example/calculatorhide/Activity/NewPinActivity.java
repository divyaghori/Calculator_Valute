package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.SecurityDatabase;
import com.example.calculatorhide.R;


public class NewPinActivity extends AppCompatActivity {

    EditText pin_et;
    Activity activity;
    TextView maintext,enter,submit_btn;
    ImageView back;
    SecurityDatabase securityDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanepassword);
        activity = this;
        securityDatabase = SecurityDatabase.getDatabse(activity);
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
                    securityDatabase.securityDao().updatePassword(pin);
                    MyApplication.SetStringToPrefs(activity,MyApplication.PIN,pin);
                    Intent i = new Intent(NewPinActivity.this, SettingActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        });
    }
}