package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAudioActivity extends AppCompatActivity {

    GridView gridview;
    String[] items;
    ImageAdapter ImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_audio);
        gridview = findViewById(R.id.gridview);
        displaysong();
    }
    ArrayList<File> findsong(File getfile) {
        ArrayList<File> files = new ArrayList<>();
        File[] file = getfile.listFiles();
        for (File singlefile : file) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                files.addAll(findsong(singlefile));
            } else {
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
                    files.add(singlefile);
                }
            }
        }
        return files;
    }
    public void displaysong() {
        final ArrayList<File> msongs = findsong(Environment.getExternalStorageDirectory());
        items = new String[msongs.size()];
        for (int i = 0; i < msongs.size(); i++) {
            items[i] = msongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
        }
        ImageAdapter = new ImageAdapter();
        gridview.setAdapter(ImageAdapter);
    }
    public class ImageAdapter extends BaseAdapter {
        List<Audio> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        String title;
        public ImageAdapter() {
//            mContext = context;
//            mInflater = LayoutInflater.from(mContext);
//            mSparseBooleanArray = new SparseBooleanArray();
//            mList = new ArrayList<Audio>();
//            this.mList = imageList;
//            imageLoader = ImageLoader.getInstance();
//            this.title = title;
        }
        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();
            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i).getTitle());
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return items.length;
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
            convertView = getLayoutInflater().inflate(R.layout.item_audio_holder, null);
            TextView textView = (TextView) convertView.findViewById(R.id.itemView_acFilesHolder_FileNameTV);
            textView.setText(items[position]);
//            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
//            mCheckBox.setTag(position);
//            mCheckBox.setChecked(mSparseBooleanArray.get(position));
//            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            return convertView;
        }

        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}