package com.example.calculatorhide.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.example.calculatorhide.R;

public class ImageViewpagerActivity extends AppCompatActivity {

    ViewPager mViewPager;
    int[] images = {R.drawable.app_icon_0};
    ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewpager);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(ImageViewpagerActivity.this, images);
        mViewPager.setAdapter(mViewPagerAdapter);
    }
}