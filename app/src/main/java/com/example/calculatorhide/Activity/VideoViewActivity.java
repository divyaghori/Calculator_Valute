package com.example.calculatorhide.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.example.calculatorhide.Utils.Util;
import com.example.calculatorhide.database.DBController;
import com.example.calculatorhide.databinding.ActivityVideoViewBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class VideoViewActivity extends AppCompatActivity {
    private Activity activity = this;
    private ActivityVideoViewBinding binding;
    private MediaItem media;
   
    HideFiles hideFiles;
    String path;
    private boolean isAdShowen;
    private InterstitialAdManager manager;
    ImageView back;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DBController(this);
        manager = new InterstitialAdManager();
        manager.fetchAd(this, true);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Util.activityData_list.contains("VideoViewActivity")) {
            isAdShowen = false;
        } else {
            isAdShowen = true;
            Util.activityData_list.add("VideoViewActivity");
        }
        
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
            media = db.getFilesByPath(path);
        }
        initUi();
    }

    private void initUi() {
        hideFiles = new HideFiles(activity);

        if (media != null) {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(binding.vvImg);
            binding.vvImg.setMediaController(mediaController);
            binding.vvImg.setVideoPath(media.getPath());
            binding.vvImg.start();
        }
        binding.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo(media);
            }
        });
        binding.ivRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRcyclePopup(media);
            }
        });
        binding.ivUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdShowen) {
                    InterstitialAd interstitialAd = manager.showIfItAvaible();
                    if (interstitialAd != null) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                showUnHideRcyclePopup(media);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                showUnHideRcyclePopup(media);
                            }
                        });
                        interstitialAd.show(VideoViewActivity.this);
                    }
                } else {
                    showUnHideRcyclePopup(media);
                }
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

    public void showRcyclePopup(MediaItem item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.recycle_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.recycle));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.yes));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.no));
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addtoRecycle(1, item.getPath());
                alertDialog.dismiss();
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

    public void showInfo(MediaItem media) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.file_info, null);
        dialogBuilder.setView(dialogView);
        TextView fileinfo = dialogView.findViewById(R.id.fileinfo);
        TextView currentpath = dialogView.findViewById(R.id.currentpath);
        TextView originpath = dialogView.findViewById(R.id.originpath);
        TextView addedon = dialogView.findViewById(R.id.addedon);
        fileinfo.setText(SplashActivity.resources.getString(R.string.file_info));
        currentpath.setText(SplashActivity.resources.getString(R.string.Current_Path));
        originpath.setText(SplashActivity.resources.getString(R.string.Original_Path));
        addedon.setText(SplashActivity.resources.getString(R.string.Added_On));
        TextView tvCrPath = dialogView.findViewById(R.id.tvCurentPath);
        TextView tvDsPath = dialogView.findViewById(R.id.tvOrPath);
        TextView tvAdded = dialogView.findViewById(R.id.tvAddedOn);
        TextView ok = dialogView.findViewById(R.id.ok);
        AlertDialog alertDialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvDsPath.setText(media.getoPath());
        tvCrPath.setText(media.getPath());
        File f = new File(media.getoPath());
        tvAdded.setText(String.valueOf(media.getTime()));
        alertDialog.show();
    }

    public void showUnHideRcyclePopup(MediaItem item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.unhide_recycle_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.unhideSelected));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.yes));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.no));
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                List<MediaItem> itemList = new ArrayList<>();
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

    public String getLastModifiedTimeInMillis(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getLastModifiedTimeFromBasicFileAttrs(file);
        } else {
            return String.valueOf(file.lastModified());
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String getLastModifiedTimeFromBasicFileAttrs(File file) {
        BasicFileAttributes basicFileAttributes = null;
        try {
            basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basicFileAttributes.creationTime().toString();
    }
}