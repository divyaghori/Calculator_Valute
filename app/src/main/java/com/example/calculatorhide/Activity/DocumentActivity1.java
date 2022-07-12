package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.calculatorhide.Adapter.DocumentAdapter;
import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.HideFiles;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentActivity1 extends AppCompatActivity {
    HidedDatabase hidedDatabase;
    Activity activity;
    ImageView icback, getimage;
    GridView gvGallery;
    TextView tvNoData,maintext;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE = 123;
    private List<String> file_uris = new ArrayList<>();
    private List<MediaItem> mediaItems = new ArrayList<>();
    private DocumentAdapter adapter;
    ImageView image;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        activity = this;
        hidedDatabase = HidedDatabase.getDatabse(activity);
        findId();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void findId() {

        hideFiles = new HideFiles(activity);
        dialogue = new CustomProgressDialogue(activity);
        icback = findViewById(R.id.back);
        gvGallery = findViewById(R.id.gvGallery);
        tvNoData = findViewById(R.id.tvNodata);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Documents));
        tvNoData.setText(SplashActivity.resources.getString(R.string.No_files_added));
        image = findViewById(R.id.image);
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
                Intent i = new Intent(activity, ImportDocument.class);
                startActivityForResult(i, 123);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                getDocument();
                dialogue.dismiss();

            }

            @Override
            public void onLoading(boolean value) {
                dialogue.show();
            }
        });
    }

    private void getDocument() {
        mediaItems.clear();
        mediaItems = hidedDatabase.mediaDao().getImagesMedia("doc", 0);
        if (mediaItems.size() != 0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new DocumentAdapter(activity, mediaItems);
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setClickItemInterface(new DocumentAdapter.ClickItemInterface() {
                @Override
                public void onItemClick(int position, String path) {
                    File file = new File(path);
                    if (file.exists()) {
                        Uri filepath = Uri.fromFile(file);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(filepath, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.e("error", "" + e);
                        }
                    } else {
                    }
                }
                @Override
                public void onItemLongClick(int position, MediaItem item) {
                    showUnHideRcyclePopup(position, item);
                }
            });

        } else {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }

    }

    private void loadDocInReader(String doc) {
        File file = new File(doc);
        if (file.exists()) {
            Uri filepath = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(filepath, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("error", "" + e);
            }
        } else {

        }
    }

    public File getFolder() {
        String rootPath = "";
        String path = ".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files";
            file = new File(rootPath);
        } else {
            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files";
//            rootPath = getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
            Log.d("root", rootPath);
            file = new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (data != null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "doc", getFolder());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDocument();
                    }
                }, 1000);

            }

        }
    }

    @Override
    protected void onResume() {
        getDocument();
        super.onResume();

    }

    public void showUnHideRcyclePopup(int position, MediaItem item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MediaItem> itemList = new ArrayList<>();
                itemList.add(item);
                hideFiles.unHideFile(itemList);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidedDatabase.mediaDao().addtoRecycle(1, item.getPath());
                getDocument();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DocumentActivity1.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}