package com.example.calculatorhide.Activity;

import static com.example.calculatorhide.Utils.RealPathUtil.isExternalStorageDocument;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView;

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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GalleryActivity extends AppCompatActivity {
    HidedDatabase hidedDatabase;
    Activity activity;
    ImageView icback, getimage;
    GridView gvGallery;
    TextView tvNoData;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE = 123;
    private List<String> file_uris = new ArrayList<>();
    private List<MediaItem> mediaItems = new ArrayList<>();
    private GalleryAdapter adapter;
    TextView maintext, filenotfound;
    private AdView mAdView;
    ImageView image;
    CheckBox check;
    private List<MediaItem> selectedItems = new ArrayList<>();
    ImageView unlock;
    boolean checked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        activity = this;
        hidedDatabase = HidedDatabase.getDatabse(activity);
//        hidedDatabase= Room.databaseBuilder(activity, HidedDatabase.class,"hidedDb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
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
        image = findViewById(R.id.image);
        check = findViewById(R.id.check);
        unlock = findViewById(R.id.unlock);
        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getimage = findViewById(R.id.getimage);
        maintext = findViewById(R.id.maintext);
        maintext.setText("Gallery");
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GalleryActivity.this, MultiPhotoSelectActivity.class);
                startActivityForResult(i, 123);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                getImages();
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
                showUnHideRcyclePopup1(0, selectedItems);
            }
        });
    }

    private void getImages() {
        mediaItems.clear();
        mediaItems = hidedDatabase.mediaDao().getImagesMedia("image", 0);
        if (mediaItems.size() != 0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new GalleryAdapter(activity, mediaItems, checked);
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }
    }

    public void btnChoosePhotosClick() {
            selectedItems = adapter.getCheckedItems();
            if (selectedItems.size() == 0) {
                unlock.setVisibility(View.GONE);
            } else {
                unlock.setVisibility(View.VISIBLE);
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
        if (requestCode == REQ_CODE) {
            if (data != null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "image", getFolder());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getImages();
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected void onResume() {
        getImages();
        super.onResume();
    }

    public void showUnHideRcyclePopup(int position, MediaItem item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
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
                getImages();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public void showUnHideRcyclePopup1(int position, List<MediaItem> itemList) {
        MediaItem item = itemList.get(position);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<MediaItem> itemList = new ArrayList<>();
//                itemList.add(item);
                hideFiles.unHideFile(itemList);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setVisibility(View.GONE);
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidedDatabase.mediaDao().addtoRecycle(1, item.getPath());
                getImages();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(GalleryActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public class GalleryAdapter extends ArrayAdapter<MediaItem> {
        private Context mContext;
        private List<MediaItem> mitemList;
        boolean cheked;
        SparseBooleanArray mSparseBooleanArray;

        public GalleryAdapter(Context context, List<MediaItem> itemList, boolean checked) {
            super(context, 0, itemList);
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
//            if(checked == true) {
//                mCheckBox.setVisibility(View.VISIBLE);
//            }
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            if (item != null) {
                if (item.getType().equalsIgnoreCase("image")) {
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
                            showUnHideRcyclePopup(position, item);
                            return false;
                        }
                    });
                }
                if (item.getType().equalsIgnoreCase("video")) {
                    Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(item.getPath(),MediaStore.Images.Thumbnails.MINI_KIND);
                    Glide.with(mContext)
                            .load(bitmap2)
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
                            showUnHideRcyclePopup(position, item);
                            return false;
                        }
                    });
                }

            }
            return view;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }

}