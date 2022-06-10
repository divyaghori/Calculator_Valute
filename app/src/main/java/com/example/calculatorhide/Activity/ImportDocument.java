package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.calculatorhide.Adapter.AdapterFilesHolder;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;

import java.io.File;
import java.util.ArrayList;

public class ImportDocument extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ModelFilesHolder> itemsList;
    String checkFileFormat;
    AdapterFilesHolder adapterFilesHolder;
    ProgressBar loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_document);
        checkFileFormat = "All Docs";
        itemsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.acFilesHolder_RecyclerView);
        loadingbar = (ProgressBar) findViewById(R.id.acFilesHolder_loadingBar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        getDocumentFiles();
//        File file = new File(String.valueOf(Environment.getExternalStorageDirectory()));
//        allDocsFiles(file);
    }

    public void getDocumentFiles() {
        new AsyncTask<Void, Void, Void>() { // from class: example.own.allofficefilereader.activities.ActivityFilesHolder.10
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingbar.setVisibility(0);
                recyclerView.setVisibility(4);
                if (itemsList.size() != 0) {
                    itemsList.clear();
                }
            }

            public Void doInBackground(Void... voidArr) {
                File file = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                if (checkFileFormat.equals("All Docs")) {
                    allDocsFiles(file);
                    return null;
                } else {
                    allDocsFiles(file);
                    return null;
                }
            }

            public void onPostExecute(Void r4) {
                super.onPostExecute(r4);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingbar.setVisibility(4);
                        recyclerView.setVisibility(0);
                        adapterFilesHolder = new AdapterFilesHolder(getApplicationContext(), ImportDocument.this, itemsList);
                        recyclerView.setAdapter(adapterFilesHolder);
                    }
                }, 100);
            }
        }.execute(new Void[0]);
    }

    public void allDocsFiles(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory() && !listFiles[i].getAbsolutePath().contains(".galleryvault_DoNotDelete") && !listFiles[i].getAbsolutePath().contains(".Shared")) {
                    allDocsFiles(listFiles[i]);
                } else if ((listFiles[i].getName().endsWith(".pdf") || listFiles[i].getName().endsWith(".doc") || listFiles[i].getName().endsWith(".doc") || listFiles[i].getName().endsWith(".ppt") || listFiles[i].getName().endsWith(".xlsx") || listFiles[i].getName().endsWith(".xls") || listFiles[i].getName().endsWith(".html") || listFiles[i].getName().endsWith(".pptx") || listFiles[i].getName().endsWith(".txt") || listFiles[i].getName().endsWith(".xml") || listFiles[i].getName().endsWith(".rtf")) && !this.itemsList.contains(listFiles[i])) {
                    this.itemsList.add(new ModelFilesHolder(listFiles[i].getName(), listFiles[i].getAbsolutePath(), false));
                }
            }
        }
//        adapterFilesHolder = new AdapterFilesHolder(getApplicationContext(), ImportDocument.this, itemsList);
//        recyclerView.setAdapter(adapterFilesHolder);
    }

}