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

import com.example.calculatorhide.Model.SecurityDatabase;
import com.example.calculatorhide.Model.Securityitem;
import com.example.calculatorhide.R;

public class NewPasswordActivity extends AppCompatActivity {

    TextView confirm_button,yourpassword,confirm;
    ImageView back;
    TextView maintext;
    SecurityDatabase securityDatabase;
    Activity activity;
    Securityitem getquepass;
    EditText answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        confirm_button = findViewById(R.id.confirm_button);
        activity = this;
        securityDatabase = SecurityDatabase.getDatabse(activity);
        getquepass = new Securityitem();
        getquepass = securityDatabase.securityDao().getqueans();
        back = findViewById(R.id.back);
        maintext = findViewById(R.id.maintext);
        answer = findViewById(R.id.answer);
        maintext.setText(SplashActivity.resources.getString(R.string.Enter_your_Password));
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
                if (answer.getText().toString().equalsIgnoreCase(getquepass.getPassword().toString())){
                    Intent i = new Intent(NewPasswordActivity.this,securityquestionActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(activity,"Please Enter Currect Password",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}