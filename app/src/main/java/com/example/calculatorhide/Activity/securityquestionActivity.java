package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculatorhide.R;

public class securityquestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner question;
    String[] security = {
            SplashActivity.resources.getString(R.string.Where_were_your_born),
            SplashActivity.resources.getString(R.string.When_is_your_birthday),
            SplashActivity.resources.getString(R.string.Who_is_your_motherfather),
            SplashActivity.resources.getString(R.string.Who_is_your_brothersister),
            SplashActivity.resources.getString(R.string.Who_is_your_girlfriendboyfriend),
    };
    TextView maintext,oldsecurity,selectquestion,enteryourans,confirm,hinit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestion);
        question = findViewById(R.id.question_spinner);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Set_security_question));
        oldsecurity = findViewById(R.id.oldsecurity);
        oldsecurity.setText(SplashActivity.resources.getString(R.string.Your_old_security_question));
        selectquestion = findViewById(R.id.selectquestion);
        selectquestion.setText(SplashActivity.resources.getString(R.string.Select_security_question));
        enteryourans = findViewById(R.id.enteryouranswer);
        enteryourans.setText(SplashActivity.resources.getString(R.string.Enter_your_answer));
        confirm = findViewById(R.id.submit_btn);
        confirm.setText(SplashActivity.resources.getString(R.string.Confirm));
        hinit = findViewById(R.id.hinit);
        hinit.setText(SplashActivity.resources.getString(R.string.Press_112));
        question.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                security);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        question.setAdapter(ad);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        ((TextView) view).setTextColor(Color.WHITE);

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }
}