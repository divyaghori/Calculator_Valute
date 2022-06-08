package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.R;

public class AdioActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_PICK_FILE_CODE = 3;
    ImageView icback,getimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
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
//                startActivityForResult(getFileChooser(),
//                        INTENT_REQUEST_PICK_FILE_CODE);
                Intent i = new Intent(AdioActivity.this,MultiAudioSelectActivity.class);
                startActivity(i);
//                Intent i = new Intent(AdioActivity.this,FileListActivity.class);
//                String path = Environment.getExternalStorageDirectory().getPath();
//                i.putExtra("path",path);
//                startActivity(i);
            }
        });
    }
    public Intent getFileChooser() {
        String folderPath = Environment.getExternalStorageDirectory() + "/";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri myUri = Uri.parse(folderPath);
        intent.setDataAndType(myUri, "application/pdf");
        return Intent.createChooser(intent, "Select a file");
    }
}