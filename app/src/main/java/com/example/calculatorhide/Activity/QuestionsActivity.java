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


public class QuestionsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestion);
        activity = this;
        question_sp = findViewById(R.id.question_spinner);
        question_sp.setOnItemSelectedListener(this);
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
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = answer_et.getText().toString();
                if (ans.length() == 0) {
                    Toast.makeText(activity, "Please Enter Answer", Toast.LENGTH_SHORT).show();
                } else {
                    //if (!MyApplication.CheckPrefs(QuestionsActivity.this, MyApplication.ANSWER)) {
                        MyApplication.SetStringToPrefs(QuestionsActivity.this, MyApplication.QUESTION, question_sp.getSelectedItem().toString());
                        MyApplication.SetStringToPrefs(QuestionsActivity.this, MyApplication.ANSWER, ans);
                        startActivity(new Intent(activity, HomeActivity.class));
                        finish();
//                    } else {
//                        if (ans.equalsIgnoreCase(MyApplication.GetStringFromPrefs(QuestionsActivity.this, MyApplication.ANSWER))
//                                && question_sp.getSelectedItem().toString() == MyApplication.GetStringFromPrefs(QuestionsActivity.this, MyApplication.QUESTION)) {
//                            MyApplication.RemovePrefs(QuestionsActivity.this, MyApplication.PIN);
//                            finish();
//                        } else {
//                            Toast.makeText(QuestionsActivity.this, "Make Sure Your Question and Answer is Right!", Toast.LENGTH_LONG).show();
//                        }
//                    }
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(Color.WHITE);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}