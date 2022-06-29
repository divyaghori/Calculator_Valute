package com.example.calculatorhide.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.calculatorhide.Adapter.AdapterFilesHolder;
import com.example.calculatorhide.Adapter.MainConstant;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImportDocument extends AppCompatActivity {
    GridView recyclerView;
    ArrayList<ModelFilesHolder> itemsList;
    String checkFileFormat;
    AdapterFilesHolder adapterFilesHolder;
    ProgressBar loadingbar;
    ImageAdapter imageAdapter;
    ImageView back;
    TextView count;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_document);
        activity=this;
        count = findViewById(R.id.count);
        checkFileFormat = "All Docs";
        itemsList = new ArrayList<>();
        recyclerView = (GridView) findViewById(R.id.acFilesHolder_RecyclerView);
        loadingbar = (ProgressBar) findViewById(R.id.acFilesHolder_loadingBar);
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
        getDocumentFiles();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void btnChoosePhotosClick() {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        if(imageAdapter.getCheckedItems().size() == 0){
            count.setText("0");
        }else{
            count.setText(String.valueOf(imageAdapter.getCheckedItems().size()));
        }
        Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
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
                        imageAdapter = new ImageAdapter(getApplicationContext(), itemsList);
                        adapterFilesHolder = new AdapterFilesHolder(getApplicationContext(), ImportDocument.this, itemsList);
                        recyclerView.setAdapter(imageAdapter);
                        Timer T = new Timer();
                        T.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnChoosePhotosClick();
                                    }
                                });
                            }
                        }, 1000, 1000);
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
    }
    public  class ImageAdapter extends BaseAdapter {
         List<ModelFilesHolder> mList;
        LayoutInflater mInflater;
        Context mContext;
         SparseBooleanArray mSparseBooleanArray;
        String title;

        public ImageAdapter(Context context, List<ModelFilesHolder> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<ModelFilesHolder>();
            this.mList = imageList;
            this.title = title;
        }

        public  ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();
            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i).getFileName());
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_audio_holder, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.itemView_acFilesHolder_FileNameTV);
            textView.setText(mList.get(position).getFileName());

            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
//            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
//            Glide.with(getApplicationContext()).load(imageUrls.get(position))
//                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
//                    .into(imageView);
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            return convertView;
        }
        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }

}