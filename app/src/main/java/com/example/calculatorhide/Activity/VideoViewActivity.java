package com.example.calculatorhide.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.databinding.ActivityImageFullViewBinding;
import com.example.calculatorhide.databinding.ActivityVideoViewBinding;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class VideoViewActivity extends AppCompatActivity {
    private Activity activity=this;
    private ActivityVideoViewBinding binding;
    private MediaItem media;
    HidedDatabase hidedDatabase;
    HideFiles hideFiles;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVideoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        binding.ivUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                List<MediaItem> itemList=new ArrayList<>();
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