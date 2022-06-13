package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calculatorhide.R;

public class GalleryActivity extends AppCompatActivity {

    ImageView icback,getimage;
    TextView maintext,filenotfound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
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
                Intent i = new Intent(GalleryActivity.this,MultiPhotoSelectActivity.class);
                startActivity(i);
            }
        });
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Gallery));
        filenotfound = findViewById(R.id.filenotfound);
        filenotfound.setText(SplashActivity.resources.getString(R.string.No_files_added));
    }
}