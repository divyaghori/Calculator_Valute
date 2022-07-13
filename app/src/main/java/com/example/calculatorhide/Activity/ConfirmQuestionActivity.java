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


public class ConfirmQuestionActivity extends AppCompatActivity {

    TextView question,maintext,submit_btn;
    EditText answer;
    ImageView back;
    Activity activity;
    Securityitem getquepass;
    SecurityDatabase securityDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_question);
        activity = this;
        securityDatabase = SecurityDatabase.getDatabse(activity);
        getquepass = new Securityitem();
        getquepass = securityDatabase.securityDao().getqueans();
        question = findViewById(R.id.question);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Set_security_question));
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setText(SplashActivity.resources.getString(R.string.Confirm));
        question.setText(getquepass.getQuestion().toString());
        answer = findViewById(R.id.answer_et);
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer.getText().toString().equalsIgnoreCase(getquepass.getAnswer().toString())){
                    startActivity(new Intent(ConfirmQuestionActivity.this,NewPinActivity.class));
                }else {
                    Toast.makeText(activity,"Please Enter Currect Answer",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}