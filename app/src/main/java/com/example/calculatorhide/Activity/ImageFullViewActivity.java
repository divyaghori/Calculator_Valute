package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.databinding.ActivityImageFullViewBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
public class ImageFullViewActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityImageFullViewBinding binding;
    private MediaItem media;
    HidedDatabase hidedDatabase;
    HideFiles hideFiles;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityImageFullViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        hidedDatabase=HidedDatabase.getDatabse(activity);
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        if(getIntent()!=null)
        {
            path=getIntent().getStringExtra("path");
            media=hidedDatabase.mediaDao().getFilesByPath(path);
        }
        initUi();
    }

    private void initUi() {
         hideFiles=new HideFiles(activity);

        if(media!=null)
        {
            Glide.with(activity)
                    .load(media.getPath())
                    .centerCrop()
                    .into(binding.ivImg);
        }
        //
        binding.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(media);
            }
        });
        binding.ivUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InterstitialAd interstitialAd = GoogleAds.getpreloadFullAds(activity);
                if (interstitialAd != null) {
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            GoogleAds.loadpreloadFullAds(activity);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Log.e("Home : ", "Error : " + adError);
                        }
                    });
                    interstitialAd.show(activity);
                } else {
                    Log.e("Home : ", "in Else part");
                }
                showUnHideRcyclePopup(media);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {

            }

            @Override
            public void onLoading(boolean value) {

            }
        });
    }
    public void showInfo(MediaItem media)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.file_info, null);
        dialogBuilder.setView(dialogView);
        TextView tvCrPath =dialogView.findViewById(R.id.tvCurentPath);
        TextView tvDsPath =dialogView.findViewById(R.id.tvOrPath);
        TextView tvAdded=dialogView.findViewById(R.id.tvAddedOn);
        //
        tvDsPath.setText(media.getOrPath());
        tvCrPath.setText(media.getPath());
        File f=new File(media.getOrPath());
        tvAdded.setText(String.valueOf(media.getTime()));
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    public void showUnHideRcyclePopup(MediaItem item)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.unhide_recycle_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide =dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle =dialogView.findViewById(R.id.tvRecycleBin);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                List<MediaItem>itemList=new ArrayList<>();
                itemList.add(item);
                hideFiles.unHideFile(itemList);
                onBackPressed();
            }
        });
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public String getLastModifiedTimeInMillis(File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getLastModifiedTimeFromBasicFileAttrs(file);
        } else {
            return String.valueOf(file.lastModified());
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String getLastModifiedTimeFromBasicFileAttrs(File file) {
        BasicFileAttributes basicFileAttributes=null;
        try {
             basicFileAttributes= Files.readAttributes(file.toPath(),BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basicFileAttributes.creationTime().toString();
    }
}