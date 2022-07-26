package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.calculatorhide.Adapter.RecycleBinAdapter;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.database.DBController;
import com.example.calculatorhide.databinding.ActivityRecycleBinBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecycleBinActivity extends AppCompatActivity {
    private ActivityRecycleBinBinding binding;
    private List<MediaItem>dataList=new ArrayList<>();
    private RecycleBinAdapter adapter;
    private HideFiles hideFiles;
    AdView mAdView;
    Activity activity;
    ImageView image,back;
    TextView tvNoData,maintext;
    DBController dbController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRecycleBinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       
        dbController = new DBController(getApplicationContext());
        initUi();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    public void initUi()
    {
        tvNoData = findViewById(R.id.tvNodata);
        maintext = findViewById(R.id.maintext);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Recycle_Bin));
        tvNoData.setText(SplashActivity.resources.getString(R.string.No_files_added));
        image = findViewById(R.id.image);
        dataList=dbController.getRecycleData(1);
        hideFiles=new HideFiles(activity);
        if(dataList.size()!=0)
        {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter=new RecycleBinAdapter(activity,dataList);
            binding.lvList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setViewItemInterface(new RecycleBinAdapter.RecyclerViewItemInterface() {
                @Override
                public void onItemClick(int position, MediaItem data) {
                   showRestorePopup(position,data);
                }
            });
        }else{
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }

    }
    public void showRestorePopup(int position,MediaItem data)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.restore_file_layout, null);
        dialogBuilder.setView(dialogView);
        TextView tvDelete =dialogView.findViewById(R.id.tvDelete);
        TextView textView =dialogView.findViewById(R.id.text);
        TextView tvRestore =dialogView.findViewById(R.id.tvRestore);
        AlertDialog alertDialog = dialogBuilder.create();
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f=new File(data.getPath());
                hideFiles.delete(activity,f);
               dbController.deletepath(data.getPath());
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        tvRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dbController.addtoRecycle(0,data.getPath());
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