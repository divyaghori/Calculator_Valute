package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.R;

public class NewPasswordActivity extends AppCompatActivity {

    TextView confirm_button,yourpassword,confirm;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        confirm_button = findViewById(R.id.confirm_button);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        yourpassword = findViewById(R.id.yourpassword);
        yourpassword.setText(SplashActivity.resources.getString(R.string.Enter_your_Password));
        confirm_button.setText(SplashActivity.resources.getString(R.string.Confirm));
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewPasswordActivity.this,securityquestionActivity.class);
                startActivity(i);
            }
        });
    }
}