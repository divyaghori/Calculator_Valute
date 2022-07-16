package com.example.calculatorhide.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ImageViewpagerActivity extends AppCompatActivity {

    ViewPager mViewPager;
  //  int[] images = {R.drawable.app_icon_0};
    ViewPagerAdapter mViewPagerAdapter;
    List<MediaItem> mediaItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
       // mediaItems = (List<MediaItem>) savedInstanceState.getParcelable("path");
       // mediaItems = (List<MediaItem>) getIntent().getSerializableExtra("path");
//        mediaItems = Util.tempList;
//        Util.tempList.clear();
        int tempPos = getIntent().getExtras().getInt("path");
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(ImageViewpagerActivity.this, Util.tempList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(tempPos);
    }
}