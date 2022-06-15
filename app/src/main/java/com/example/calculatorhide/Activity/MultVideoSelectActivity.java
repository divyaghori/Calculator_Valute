package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultVideoSelectActivity extends BaseActivity {

    private ArrayList<String> imageUrls;
    private ImageAdapter imageAdapter;
    TextView count;
    private GridView gridView;
    private LinearLayout llCount;
    ArrayList<String> selectedItems=new ArrayList<>();
    Activity activity;
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
        final String[] columns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " ASC");
        imageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Video.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }
        imageAdapter = new ImageAdapter(this, imageUrls);
        gridView = (GridView) findViewById(R.id.gridview);
        llCount=findViewById(R.id.llCount);
        count =  findViewById(R.id.count);
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
        gridView.setAdapter(imageAdapter);
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != imageUrls && !imageUrls.isEmpty())
                    Toast.makeText(getApplicationContext(),
                            "position " + position + " " +
                                    imageUrls.get(position), 300).show();
                Intent i = new Intent(MultVideoSelectActivity.this,VideoPlayerActivity.class);
                i.putExtra("getvideo",imageUrls.get(position));
                startActivity(i);
            }
        });
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
        Log.d(MultVideoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
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
                convertView = mInflater.inflate(R.layout.row_multivideo_item, null);
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
}