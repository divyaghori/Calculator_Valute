package com.example.calculatorhide.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculatorhide.Adapter.GalleryAdapter;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.HideFiles;
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

public class VideoActivity extends AppCompatActivity {
    HidedDatabase hidedDatabase;
    Activity activity;
    ImageView icback,getimage;
    GridView gvGallery;
    TextView tvNoData;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE=123;
    private List<String> file_uris=new ArrayList<>();
    private List<MediaItem>mediaItems=new ArrayList<>();
    private GalleryAdapter adapter;
    TextView maintext,filenotfound;
    AdView mAdView;
    RelativeLayout tvdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        activity=this;
        maintext = findViewById(R.id.maintext);
        tvdata = findViewById(R.id.tvdata);
        maintext.setText(SplashActivity.resources.getString(R.string.Video));
        filenotfound = findViewById(R.id.tvNodata);
        filenotfound.setText(SplashActivity.resources.getString(R.string.No_files_added));
        hidedDatabase=HidedDatabase.getDatabse(activity);
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        findId();
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
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void findId() {
        hideFiles = new HideFiles(activity);
        dialogue=new CustomProgressDialogue(activity);
        icback = findViewById(R.id.back);
        gvGallery=findViewById(R.id.gvGallery);
        tvNoData=findViewById(R.id.tvNodata);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getimage = findViewById(R.id.getimage);
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,MultVideoSelectActivity.class);
                startActivityForResult(i,123);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                getVideos();
                dialogue.dismiss();

            }

            @Override
            public void onLoading(boolean value) {
                dialogue.show();
            }
        });

    }

    private void getVideos() {
        mediaItems.clear();
        mediaItems= hidedDatabase.mediaDao().getImagesMedia("video",0);
        if(mediaItems.size()!=0) {
            tvNoData.setVisibility(View.GONE);
            tvdata.setVisibility(View.GONE);
            adapter = new GalleryAdapter(activity, mediaItems);
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            adapter.setClickItemInterface(new GalleryAdapter.ClickItemInterface() {
                @Override
                public void onItemClick(int position, String path) {
                    Intent intent=new Intent(activity,VideoViewActivity.class);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(int position, MediaItem item) {
                    showUnHideRcyclePopup(position,item);
                }
            });

        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            tvdata.setVisibility(View.VISIBLE);
        }

    }

    public File getFolder()
    {
        String rootPath="";
        String path="CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rootPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+path+"/"+"files"+"/"+".vault";
            file= new File(rootPath);
        } else {
            rootPath=getExternalFilesDir(null).getAbsoluteFile()+"/"+path+"/"+"files"+"/"+".vault";
            file= new File(rootPath);
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            if(data!=null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "video", getFolder());
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getVideos();
                    }
                },1000);

            }

        }
    }

    @Override
    protected void onResume() {
        getVideos();
        super.onResume();

    }
    public void showUnHideRcyclePopup(int position, MediaItem item)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide =dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle =dialogView.findViewById(R.id.tvRecycleBin);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MediaItem>itemList=new ArrayList<>();
                itemList.add(item);
                hideFiles.unHideFile(itemList);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidedDatabase.mediaDao().addtoRecycle(1,item.getPath());
                getVideos();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(VideoActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}