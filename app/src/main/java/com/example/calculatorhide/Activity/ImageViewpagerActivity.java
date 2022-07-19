package com.example.calculatorhide.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.Utils.Util;
import com.example.calculatorhide.database.DBController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageViewpagerActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    List<MediaItem> mediaItems;
    DBController db;
    private MediaItem media;
    HideFiles hideFiles;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
        activity=this;
        db = new DBController(this);
        hideFiles=new HideFiles(activity);
        int tempPos = getIntent().getExtras().getInt("path");
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(ImageViewpagerActivity.this, Util.tempList, new ViewPagerAdapter.ViewAdapterInterface() {
            @Override
            public void onInfoClick(String click) {
                media=db.getFilesByPath(click);
                showInfo(media);
            }

            @Override
            public void onUnhideClick(String click) {
                media=db.getFilesByPath(click);
                showUnHideRcyclePopup(media);
            }

            @Override
            public void onDeleteClick(String click) {
                media=db.getFilesByPath(click);
                showRcyclePopup(media);
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
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(tempPos);
    }
    public void showInfo(MediaItem media)
    {
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
        TextView tvCrPath =dialogView.findViewById(R.id.tvCurentPath);
        TextView tvDsPath =dialogView.findViewById(R.id.tvOrPath);
        TextView tvAdded=dialogView.findViewById(R.id.tvAddedOn);
        TextView ok = dialogView.findViewById(R.id.ok);
        AlertDialog  alertDialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tvDsPath.setText(media.getoPath());
        tvCrPath.setText(media.getPath());
        File f=new File(media.getoPath());
        tvAdded.setText(String.valueOf(media.getTime()));
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
        TextView maintext = dialogView.findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.unhideSelected));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.yes));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.no));
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
    public void showRcyclePopup(MediaItem item)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.recycle_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide =dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle =dialogView.findViewById(R.id.tvRecycleBin);
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
}