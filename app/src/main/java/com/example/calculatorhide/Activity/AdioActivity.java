package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.HideFiles;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdioActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_PICK_FILE_CODE = 3;
    ImageView icback,getimage;
    TextView maintext,filenotfound;
    AdView mAdView;
    Activity activity;
    LinearLayout tvdata;
    private static final int REQ_CODE = 123;
    private List<String> file_uris = new ArrayList<>();
    HideFiles hideFiles;
    HidedDatabase hidedDatabase;
    private List<MediaItem> mediaItems = new ArrayList<>();
    TextView tvNoData;
    ImageView image;
    CustomProgressDialogue dialogue;
    AudioAdapter audioAdapter;
    GridView gvGallery;
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        findId();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (data != null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "audio", getFolder());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAudio();
                    }
                }, 1000);
            }
        }
    }
    @Override
    protected void onResume() {
        getAudio();
        super.onResume();
    }
    public File getFolder() {
        String rootPath = "";
        String path = ".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files" ;
            file = new File(rootPath);
        } else {
            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files";
//            rootPath = getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
            Log.d("root", rootPath);
            file = new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    private void findId() {
        activity=this;
        hideFiles = new HideFiles(activity);
        dialogue = new CustomProgressDialogue(activity);
        hidedDatabase = HidedDatabase.getDatabse(activity);
        gvGallery = findViewById(R.id.gvGallery);
        tvNoData = findViewById(R.id.tvNodata);
        image = findViewById(R.id.image);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        tvdata = findViewById(R.id.tvdata);
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
                Intent i = new Intent(AdioActivity.this,AudioActivity.class);
                startActivityForResult(i, 123);

//                Intent i = new Intent(AdioActivity.this,FileListActivity.class);
//                String path = Environment.getExternalStorageDirectory().getPath();
//                i.putExtra("path",path);
//                startActivity(i);
            }
        });
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Audio));
        filenotfound = findViewById(R.id.tvNodata);
        filenotfound.setText(SplashActivity.resources.getString(R.string.No_files_added));
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                getAudio();
                dialogue.dismiss();
            }

            @Override
            public void onLoading(boolean value) {
                dialogue.show();
            }
        });
    }
    private void getAudio() {
        mediaItems.clear();
        mediaItems = hidedDatabase.mediaDao().getImagesMedia("audio", 0);
        if (mediaItems.size() != 0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            audioAdapter = new AudioAdapter(activity, mediaItems, checked);
            gvGallery.setAdapter(audioAdapter);
            audioAdapter.notifyDataSetChanged();
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }
    }
    public Intent getFileChooser() {
        String folderPath = Environment.getExternalStorageDirectory() + "/";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri myUri = Uri.parse(folderPath);
        intent.setDataAndType(myUri, "application/pdf");
        return Intent.createChooser(intent, "Select a file");
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AdioActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public class AudioAdapter extends ArrayAdapter<MediaItem> {
        private Context mContext;
        private List<MediaItem> mitemList;
        boolean cheked;
        SparseBooleanArray mSparseBooleanArray;

        public AudioAdapter(Context context, List<MediaItem> itemList, boolean checked) {
            super(context, 0, itemList);
            mContext = context;
            mitemList = itemList;
            this.cheked = checked;
            mSparseBooleanArray = new SparseBooleanArray();
        }

        public ArrayList<MediaItem> getCheckedItems() {
            ArrayList<MediaItem> mTempArry = new ArrayList<MediaItem>();
            for (int i = 0; i < mitemList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mitemList.get(i));
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return mitemList.size();
        }

        @Nullable
        @Override
        public MediaItem getItem(int position) {
            return mitemList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MediaItem item = mitemList.get(position);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audio_item, parent, false);
            TextView ivItem = view.findViewById(R.id.ivItem);
            CheckBox mCheckBox = view.findViewById(R.id.checkbox);
//            if(checked == true) {
//                mCheckBox.setVisibility(View.VISIBLE);
//            }
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            if (item != null) {
                if (item.getType().equalsIgnoreCase("audio")) {
                    ivItem.setText(item.getName());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, ImageFullViewActivity.class);
                            intent.putExtra("path", mitemList.get(position).getPath());
                            startActivity(intent);
                        }
                    });
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
//                            showUnHideRcyclePopup(position, item);
                            return false;
                        }
                    });
                }
            }
            return view;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}