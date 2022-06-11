package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

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
            "Where were your born?",
            "When is your birthday?",
            "Who is your mother/father?",
            "Who is your brother/sister?",
            "Who is your girlfriend/boyfriend?"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securityquestion);
        question = findViewById(R.id.question);
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
        ((TextView) parent.getChildAt(0)).setTextColor(0xdedede);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }
}