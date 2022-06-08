package com.example.calculatorhide.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultiAudioSelectActivity extends BaseActivity {
    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    TextView count;
    private GridView gridView;
    String title;

    @SuppressLint("Range")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_grid);
        final String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID};
        final String orderBy = MediaStore.Audio.Media.DATE_ADDED;
        Cursor imagecursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null, orderBy + " DESC");
        imageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
        }
        imageAdapter = new ImageAdapter(this, imageUrls, title);
        gridView = (GridView) findViewById(R.id.gridview);
        count = findViewById(R.id.count);
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnChoosePhotosClick();
                    }
                });
            }
        }, 1000, 1000);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void btnChoosePhotosClick() {
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
        if (imageAdapter.getCheckedItems().size() == 0) {
            count.setText("0");
        } else {
            count.setText(String.valueOf(imageAdapter.getCheckedItems().size()));
        }
        Log.d(MultiAudioSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
    }

    public class ImageAdapter extends BaseAdapter {
        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        String title;

        public ImageAdapter(Context context, ArrayList<String> imageList, String title) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            this.mList = imageList;
            imageLoader = ImageLoader.getInstance();
            this.title = title;
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
                convertView = mInflater.inflate(R.layout.item_audio_holder, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.itemView_acFilesHolder_FileNameTV);
            textView.setText(title);

            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
//            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
//            Glide.with(getApplicationContext()).load(imageUrls.get(position))
//                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
//                    .into(imageView);
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