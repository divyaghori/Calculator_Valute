package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.calculatorhide.R;
import com.example.calculatorhide.toDoList.MainActivity;

public class FilemanagerActivity extends AppCompatActivity {

    TextView gallery,video,audio,document,note,maintext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        gallery = findViewById(R.id.gallery);
        video = findViewById(R.id.video);
        audio = findViewById(R.id.audio);
        document = findViewById(R.id.document);
        note = findViewById(R.id.note);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.File_Manager));
        gallery.setText(SplashActivity.resources.getString(R.string.Gallery));
        video.setText(SplashActivity.resources.getString(R.string.Video));
        audio.setText(SplashActivity.resources.getString(R.string.Audio));
        document.setText(SplashActivity.resources.getString(R.string.Documents));
        note.setText(SplashActivity.resources.getString(R.string.Notes));
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilemanagerActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilemanagerActivity.this, VideoActivity.class);
                startActivity(i);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilemanagerActivity.this, AdioActivity.class);
                startActivity(i);
            }
        });
        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilemanagerActivity.this, DocumentActivity.class);
                startActivity(i);
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilemanagerActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }


}