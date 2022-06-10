package com.example.calculatorhide.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.calculatorhide.R;
public class DocumentActivity extends AppCompatActivity {
    ImageView icback,getimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        findId();
    }
    private void findId() {
        icback = findViewById(R.id.back);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getimage = findViewById(R.id.getimage);
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DocumentActivity.this,ImportDocument.class);
                startActivity(i);
            }
        });
    }
}