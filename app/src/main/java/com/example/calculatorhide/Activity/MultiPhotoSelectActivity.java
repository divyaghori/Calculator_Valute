package com.example.calculatorhide.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.Utils.RealPathUtil;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MultiPhotoSelectActivity extends BaseActivity {
    private Activity activity;
    private ArrayList<String> imageUrls=new ArrayList<>();
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    TextView count;
    private GridView gridView;
    private LinearLayout llCount;
    private List<String>selectedItems=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);
        activity = this;
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
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " ASC");
        imageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }
//        String sort=MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
//        imageUrls.addAll(getImageList(sort));
        imageAdapter = new ImageAdapter(this, imageUrls);
        gridView = (GridView) findViewById(R.id.gridview);
        count =  findViewById(R.id.count);
        llCount=findViewById(R.id.llCount);
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        btnChoosePhotosClick();
                    }
                });
            }
        }, 1000, 1000);
       llCount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(selectedItems.size()!=0) {
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
                   Intent intent = new Intent();
                   intent.putExtra("files", (Serializable) selectedItems);
                   setResult(2, intent);
                   finish();
               }
           }
       });
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public void btnChoosePhotosClick() {
         selectedItems = imageAdapter.getCheckedItems();
        if(imageAdapter.getCheckedItems().size() == 0){
            count.setText("0");
        }else{
            count.setText(String.valueOf(imageAdapter.getCheckedItems().size()));
        }
        Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
    }
    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            this.mList = imageList;
            imageLoader = ImageLoader.getInstance();
        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_multiphoto_item, null);
            }
            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            Glide.with(getApplicationContext()).load(imageUrls.get(position))
                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
                    .into(imageView);
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            return convertView;
        }

        OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
    protected ArrayList<String> getImageList(String sort) {
        ArrayList<String> imageList = new ArrayList<>();
        Uri collection;

        final String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.SIZE,
        };

        final String sortOrder = sort;

        final String selection = MediaStore.Images.ImageColumns.MIME_TYPE + " = ?";

        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg");
//        final String mimeType2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension("png");
        final String[] selectionArgs = new String[]{mimeType};

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
//        }else{
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        }


        try (Cursor cursor =getContentResolver().query(collection, projection, selection, selectionArgs, sortOrder)) {
            assert cursor != null;

            if (cursor.moveToFirst()) {
                int mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.MIME_TYPE);
                int addedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED);
                int modifiedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED);
                int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                int titleCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE);
                int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE);
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                long fileId = cursor.getLong(columnIndex);
//                Uri fileUri = Uri.parse(collection.toString() + "/" + fileId);


                do {
                    Uri fileUri=Uri.withAppendedPath(collection,cursor.getString(columnIndex));
                    long dateAdded = cursor.getLong(addedCol);
                    long dateModified = cursor.getLong(modifiedCol);
                    long size=cursor.getLong(sizeCol);
                    String title=cursor.getString(titleCol);
                    imageList.add(fileUri.toString());

                } while (cursor.moveToNext());
            }
        }
        return imageList;
    }

}