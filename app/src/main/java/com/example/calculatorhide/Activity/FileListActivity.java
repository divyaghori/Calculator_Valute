package com.example.calculatorhide.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculatorhide.Adapter.MyAdapter;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;

import java.io.File;

public class FileListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noFilesText = findViewById(R.id.nofiles_textview);
        String path = getIntent().getStringExtra("path");
        File root = new File(path);
        File[] filesAndFolders = root.listFiles();
        if (filesAndFolders == null || filesAndFolders.length == 0) {
            noFilesText.setVisibility(View.VISIBLE);
            return;
        }
        noFilesText.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), filesAndFolders));
    }

    public File[] allDocsFiles(File file) {
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory() && !listFiles[i].getAbsolutePath().contains(".galleryvault_DoNotDelete") && !listFiles[i].getAbsolutePath().contains(".Shared")) {
                allDocsFiles(listFiles[i]);
            } else if ((listFiles[i].getName().endsWith(".pdf"))) {
                return listFiles;
//                    this.itemsList.add(new ModelFilesHolder(listFiles[i].getName(), listFiles[i].getAbsolutePath(), false));
            }
        }
        return listFiles;
    }
}