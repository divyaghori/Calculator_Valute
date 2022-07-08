package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.calculatorhide.Adapter.AdapterFilesHolder;
import com.example.calculatorhide.Adapter.AdapterFilesHolderAudio;
import com.example.calculatorhide.Model.ModelFilesHolder;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.example.calculatorhide.Utils.Util;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImportAudioActivity extends AppCompatActivity {
    GridView recyclerView;
    ArrayList<ModelFilesHolder> itemsList;
    String checkFileFormat;
    AdapterFilesHolderAudio adapterFilesHolder;
    ProgressBar loadingbar;
    ImageAdapter imageAdapter;
    ImageView back;
    TextView count;
    Activity activity;
     LinearLayout llCount;
    private List<String>selectedItems=new ArrayList<>();
    private boolean isAdShowen;
    private InterstitialAdManager manager;
    TextView searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_document);
        activity=this;
        if (Util.activityData_list.contains("ImportAudioActivity")) {
            isAdShowen = false;
        } else {
            isAdShowen = true;
            Util.activityData_list.add("ImportAudioActivity");
        }
        searchtext = findViewById(R.id.searchtext);
        searchtext.setText("Audio");
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
        manager = new InterstitialAdManager();
        manager.fetchAd(this, true);
        llCount = findViewById(R.id.llCount);
        llCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.size() != 0) {
                    if (isAdShowen) {
                        InterstitialAd interstitialAd = manager.showIfItAvaible();
                        if (interstitialAd != null) {
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    Intent intent = new Intent();
                                    intent.putExtra("files", (Serializable) selectedItems);
                                    setResult(2, intent);
                                    finish();
                                }
                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    Intent intent = new Intent();
                                    intent.putExtra("files", (Serializable) selectedItems);
                                    setResult(2, intent);
                                    finish();
                                }
                            });
                            interstitialAd.show(ImportAudioActivity.this);
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("files", (Serializable) selectedItems);
                        setResult(2, intent);
                        finish();
                    }
                }
            }
        });
    }
    public void btnChoosePhotosClick() {
        selectedItems = imageAdapter.getCheckedItems();
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
                        adapterFilesHolder = new AdapterFilesHolderAudio(getApplicationContext(), ImportAudioActivity.this, itemsList);
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
                } else if ((listFiles[i].getName().endsWith(".mp3") || listFiles[i].getName().endsWith(".wav") && !this.itemsList.contains(listFiles[i]))) {
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
                    mTempArry.add(mList.get(i).getFileUri());
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