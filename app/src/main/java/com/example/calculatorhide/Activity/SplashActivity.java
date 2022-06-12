package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {
    private Activity activity=this;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        binding.tvVersion.setText("Version "+ BuildConfig.VERSION_NAME);
//        binding.lavLoad.setProgress(1);
//        binding.lavLoad.pauseAnimation();
        binding.lavLoad.loop(true);
        binding.lavLoad.playAnimation();
//        try
//        {
//            Drawable icon = getPackageManager().getApplicationIcon("com.example.calculatorhide");
//            binding.ivIcon.setImageDrawable(icon);
//        }
//        catch ( PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
              startActivity(new Intent(activity, CalculatorActivity.class));
              finish();
            }
        }, 3000);
    }
}