package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {
    HidedDatabase hidedDatabase;
    Activity activity;
    ImageView icback,getimage;
    GridView gvGallery;
    TextView tvNoData,maintext;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE=123;
    private List<String> file_uris=new ArrayList<>();
    private List<MediaItem>mediaItems=new ArrayList<>();
    private GalleryAdapter adapter;
    ImageView image;
    AdView mAdView;
    ImageView unlock;
    CheckBox check;
    boolean checked = false;
    private List<MediaItem> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        activity=this;
        hidedDatabase=HidedDatabase.getDatabse(activity);
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        findId();

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
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Video));
        tvNoData.setText(SplashActivity.resources.getString(R.string.No_files_added));
        image = findViewById(R.id.image);
        unlock = findViewById(R.id.unlock);
        check = findViewById(R.id.check);
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
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnChoosePhotosClick();
                        checked = check.isChecked();
                    }
                });
            }
        }, 1000, 1000);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnHideRcyclePopup1(adapter.getCheckedItems());
            }
        });

    }

    private void getVideos() {
        mediaItems.clear();
        mediaItems= hidedDatabase.mediaDao().getImagesMedia("video",0);
        if(mediaItems.size()!=0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new GalleryAdapter(activity, mediaItems, checked,check.isChecked());
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    adapter.OnSelectionModeChange(isChecked);
                }
            });
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }

    }
    public void btnChoosePhotosClick() {
        if(selectedItems.size()!=0) {
            selectedItems = adapter.getCheckedItems();
            if (selectedItems.size() == 0) {
                unlock.setVisibility(View.GONE);
            } else {
                unlock.setVisibility(View.VISIBLE);
            }
        }
        Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
    }


    public File getFolder() {
        String rootPath = "";
        String path = ".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files" ;
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
        if(requestCode==REQ_CODE)
        {
            if(data!=null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "video", getFolder());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_hide_progress, null);
                dialogBuilder.setView(dialogView);
                TextView done = dialogView.findViewById(R.id.done);
                AlertDialog alertDialog = dialogBuilder.create();
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
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
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        maintext.setText(SplashActivity.resources.getString(R.string.unhide_recycle));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.unhide));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.Recycle_Bin));
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
    public void showUnHideRcyclePopup1( List<MediaItem> itemList) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        maintext.setText(SplashActivity.resources.getString(R.string.unhide_recycle));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.unhide));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.Recycle_Bin));
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<MediaItem> itemList = new ArrayList<>();
//                itemList.add(item);
                hideFiles.unHideFile(itemList);
                unlock.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setVisibility(View.GONE);
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(VideoActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public class GalleryAdapter extends ArrayAdapter<MediaItem> {
        private Boolean selectionMode;
        private Context mContext;
        private List<MediaItem> mitemList;
        boolean cheked;
        SparseBooleanArray mSparseBooleanArray;
        public void OnSelectionModeChange(Boolean selectionMode){
            this.selectionMode = selectionMode;
            notifyDataSetChanged();
        }
        public GalleryAdapter(Context context, List<MediaItem> itemList, boolean checked,Boolean selectionMode) {
            super(context, 0, itemList);
            this.selectionMode= selectionMode;
            mContext = context;
            mitemList = itemList;
            this.cheked = checked;
            mSparseBooleanArray = new SparseBooleanArray();
        }
        public ArrayList<MediaItem> getCheckedItems() {
            ArrayList<MediaItem> mTempArry = new ArrayList<MediaItem>();
            for (int i = 0; i < mitemList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mitemList.get(i));
                }
            }
            return mTempArry;
        }
        @Override
        public int getCount() {
            return mitemList.size();
        }

        @Nullable
        @Override
        public MediaItem getItem(int position) {
            return mitemList.get(position);
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MediaItem item = mitemList.get(position);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_media_item, parent, false);
            ImageView ivItem = view.findViewById(R.id.ivItem);
            CheckBox mCheckBox = view.findViewById(R.id.checkbox);
            mCheckBox.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            if (item != null) {
                if (item.getType().equalsIgnoreCase("HiddenFileType.image")) {
                    Glide.with(mContext)
                            .load(item.getPath())
                            .centerCrop()
                            .into(ivItem);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, ImageFullViewActivity.class);
                            intent.putExtra("path", mitemList.get(position).getPath());
                            startActivity(intent);
                        }
                    });
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showUnHideRcyclePopup1(mitemList);
                            return false;
                        }
                    });
                }
                if (item.getType().equalsIgnoreCase("video")) {
                    Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(item.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
                    Glide.with(mContext)
                            .load(bitmap2)
                            .centerCrop()
                            .into(ivItem);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, VideoViewActivity.class);
                            intent.putExtra("path", mitemList.get(position).getPath());
                            startActivity(intent);
                        }
                    });
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showUnHideRcyclePopup(position, item);
                            return false;
                        }
                    });
                }

            }
            return view;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (mSparseBooleanArray.size() > 0) {
//                    ShowOtherOption();
//                }else {
//                    HideOtherOption();
//                }

                //mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                if (isChecked){
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                }else {
                    mSparseBooleanArray.removeAt(mSparseBooleanArray.indexOfKey((Integer) buttonView.getTag()));
                }
                unlock.setVisibility(mSparseBooleanArray.size() > 0 ? View.VISIBLE :View.GONE);
            }
        };
    }
}