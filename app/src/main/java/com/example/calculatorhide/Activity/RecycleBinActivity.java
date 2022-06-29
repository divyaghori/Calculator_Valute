package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.calculatorhide.Adapter.RecycleBinAdapter;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.databinding.ActivityRecycleBinBinding;
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


public class RecycleBinActivity extends AppCompatActivity {
    private ActivityRecycleBinBinding binding;
    private HidedDatabase hidedDatabase;
    private List<MediaItem>dataList=new ArrayList<>();
    private RecycleBinAdapter adapter;
    private HideFiles hideFiles;
    AdView mAdView;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRecycleBinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        activity = this;

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    public void initUi()
    {
        hidedDatabase=HidedDatabase.getDatabse(activity);
        dataList=hidedDatabase.mediaDao().getRecycleData(1);
        hideFiles=new HideFiles(activity);
        if(dataList.size()!=0)
        {
            adapter=new RecycleBinAdapter(activity,dataList);
            binding.lvList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setViewItemInterface(new RecycleBinAdapter.RecyclerViewItemInterface() {
                @Override
                public void onItemClick(int position, MediaItem data) {
                   showRestorePopup(position,data);
                }
            });
        }

    }
    public void showRestorePopup(int position,MediaItem data)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.restore_file_layout, null);
        dialogBuilder.setView(dialogView);
        TextView tvDelete =dialogView.findViewById(R.id.tvDelete);
        TextView tvRestore =dialogView.findViewById(R.id.tvRestore);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f=new File(data.getPath());
                hideFiles.delete(activity,f);
                hidedDatabase.mediaDao().deleteByPath(data.getPath());
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        tvRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    hidedDatabase.mediaDao().addtoRecycle(0,data.getPath());
                    dataList.remove(position);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RecycleBinActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}