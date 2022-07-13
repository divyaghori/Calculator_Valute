package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;


public class QuestionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner question_sp;
    EditText answer_et;
    Activity activity;
    //TextView confirm_txt;
    // boolean isForConfirm;
    String[] Questions_Array = {
            SplashActivity.resources.getString(R.string.Where_were_your_born),
            SplashActivity.resources.getString(R.string.When_is_your_birthday),
            SplashActivity.resources.getString(R.string.Who_is_your_motherfather),
            SplashActivity.resources.getString(R.string.Who_is_your_brothersister),
            SplashActivity.resources.getString(R.string.Who_is_your_girlfriendboyfriend),
    };
    String password;
    HideFiles hideFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestion_main);
        activity = this;
        hideFiles = new HideFiles(activity);
        question_sp = findViewById(R.id.question_spinner);
        question_sp.setOnItemSelectedListener(this);
        password = getIntent().getStringExtra("password");
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                Questions_Array);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        question_sp.setAdapter(ad);
        answer_et = findViewById(R.id.answer_et);
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
            }
            @Override
            public void onLoading(boolean value) {
            }
        });
        findViewById(R.id.skip_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFiles.createSecurity(password,null,null);
                Intent i = new Intent(QuestionsActivity.this, CalcualatorTransitionActivity.class);
                i.putExtra("index", 0);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = answer_et.getText().toString();
                if (ans.length() == 0) {
                    Toast.makeText(activity, "Please Enter Answer", Toast.LENGTH_SHORT).show();
                } else {
                    hideFiles.createSecurity(password,question_sp.getSelectedItem().toString(),ans);
                    MyApplication.SetStringToPrefs(QuestionsActivity.this, MyApplication.QUESTION, question_sp.getSelectedItem().toString());
                    MyApplication.SetStringToPrefs(QuestionsActivity.this, MyApplication.ANSWER, ans);
                    Intent i = new Intent(QuestionsActivity.this, CalcualatorTransitionActivity.class);
                    i.putExtra("index", 0);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(Color.WHITE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}