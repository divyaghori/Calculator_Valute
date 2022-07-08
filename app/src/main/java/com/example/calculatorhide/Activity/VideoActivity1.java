package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.Adapter.AudioAdapter;
import com.example.calculatorhide.Adapter.GalleryAdapter;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.HideFiles;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoActivity1 extends AppCompatActivity {
    HidedDatabase hidedDatabase;
    Activity activity;
    ImageView icback,getimage;
    GridView gvGallery;
    TextView tvNoData;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE=123;
    private List<String> file_uris=new ArrayList<>();
    private List<MediaItem>mediaItems=new ArrayList<>();
    private AudioAdapter adapter;
    ImageView image;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        activity=this;
        hidedDatabase=HidedDatabase.getDatabse(activity);
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        findId();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void findId() {
        hideFiles = new HideFiles(activity);
        dialogue=new CustomProgressDialogue(activity);
        icback = findViewById(R.id.back);
        gvGallery=findViewById(R.id.gvGallery);
        tvNoData=findViewById(R.id.tvNodata);
        image = findViewById(R.id.image);
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
                Intent i = new Intent(activity,ImportAudioActivity.class);
                startActivityForResult(i,123);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
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
        mediaItems= hidedDatabase.mediaDao().getImagesMedia("audio",0);
        if(mediaItems.size()!=0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new AudioAdapter(activity, mediaItems);
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setClickItemInterface(new AudioAdapter.ClickItemInterface() {
                @Override
                public void onItemClick(int position, String path) {
                    Intent intent=new Intent(activity,VideoViewActivity.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(int position, MediaItem item) {
                    showUnHideRcyclePopup(position,item);
                }
            });

        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            if(data!=null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "audio", getFolder());
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAudio();
                    }
                },1000);

            }

        }
    }

    @Override
    protected void onResume() {
        getAudio();
        super.onResume();

    }
    public void showUnHideRcyclePopup(int position, MediaItem item)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide =dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle =dialogView.findViewById(R.id.tvRecycleBin);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MediaItem>itemList=new ArrayList<>();
                itemList.add(item);
                hideFiles.unHideFile(itemList);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidedDatabase.mediaDao().addtoRecycle(1,item.getPath());
                getAudio();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(VideoActivity1.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}