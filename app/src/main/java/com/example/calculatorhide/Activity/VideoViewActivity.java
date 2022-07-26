package com.example.calculatorhide.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.example.calculatorhide.Utils.Util;
import com.example.calculatorhide.database.DBController;
import com.example.calculatorhide.databinding.ActivityVideoViewBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.video.VideoSize;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.otaliastudios.zoom.ZoomLogger;
import com.otaliastudios.zoom.ZoomSurfaceView;

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
     ExoPlayer player;
     RelativeLayout topbar;
     PlayerControlView player_control_view;
     ZoomSurfaceView zoom_surface;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ZoomLogger.setLogLevel(ZoomLogger.LEVEL_VERBOSE);
        super.onCreate(savedInstanceState);
        binding = ActivityVideoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DBController(this);
        manager = new InterstitialAdManager();
        manager.fetchAd(this, true);
        topbar = findViewById(R.id.topbar);
        player_control_view = findViewById(R.id.player_control_view);
        zoom_surface = findViewById(R.id.surface_view);
        zoom_surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    topbar.setVisibility(View.VISIBLE);
                    player_control_view.setVisibility(View.VISIBLE);
                    player_control_view.show();
                }else{
                    topbar.setVisibility(View.GONE);
                    player_control_view.setVisibility(View.GONE);
                }
                isPlaying = !isPlaying;
            }
        });
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
        if (getIntent() != null) {
            path = getIntent().getStringExtra("path");
            media = db.getFilesByPath(path);
        }
        initUi();
        final boolean supportsSurfaceView = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        if (supportsSurfaceView) setUpVideoPlayer();
//        player.setPlayWhenReady(true);

    }

    private void initUi() {
        hideFiles = new HideFiles(activity);
        if (media != null) {
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(binding.vvImg);
//            binding.vvImg.setMediaController(mediaController);
//            binding.vvImg.setVideoPath(media.getPath());
//            binding.vvImg.start();
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



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setUpVideoPlayer() {
        player = new ExoPlayer.Builder(this).build();
        PlayerControlView controls = findViewById(R.id.player_control_view);
        final ZoomSurfaceView surface = findViewById(R.id.surface_view);
        player.addListener(new Player.Listener() {
            @Override
            public void onVideoSizeChanged(@NonNull VideoSize videoSize) {
                surface.setContentSize(videoSize.width, videoSize.height);
            }
        });
        surface.addCallback(new ZoomSurfaceView.Callback() {
            @Override
            public void onZoomSurfaceCreated(@NonNull ZoomSurfaceView view) {
                player.setVideoSurface(view.getSurface());
            }
            @Override
            public void onZoomSurfaceDestroyed(@NonNull ZoomSurfaceView view) { }
        });
        controls.setPlayer(player);
        controls.setShowTimeoutMs(0);
        controls.show();
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this);
        Uri videoUri = Uri.parse(media.getPath());
        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(com.google.android.exoplayer2.MediaItem.fromUri(videoUri));
        player.setMediaSource(videoSource);
        player.prepare();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ZoomSurfaceView surface = findViewById(R.id.surface_view);
        surface.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        ZoomSurfaceView surface = findViewById(R.id.surface_view);
        surface.onResume();
    }
}