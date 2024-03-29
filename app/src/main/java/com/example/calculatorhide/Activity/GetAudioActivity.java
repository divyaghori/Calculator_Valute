package com.example.calculatorhide.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculatorhide.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GetAudioActivity extends AppCompatActivity {

    GridView gridview;
    String[] items;
    ImageAdapter ImageAdapter;
    TextView count;
    ActivityResultLauncher<String> storagePermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_audio);
        gridview = findViewById(R.id.gridview);
        displaysong();
        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result){
                displaysong();
            }
            else {
                respondOnUserPermissionActs();
            }
        });
        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void respondOnUserPermissionActs() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            displaysong();
        }
        else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Requesting Permission")
                    .setMessage("Allow us to fetch & show songs on your device")
                    .setPositiveButton("Allow ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }).setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext()," You denied to fetch songs", Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                }
            })
                    .show();
        }
        else{
            Toast.makeText(this, "You denied to fetch songs", Toast.LENGTH_LONG).show();
        }
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
        ImageAdapter = new ImageAdapter();
        gridview.setAdapter(ImageAdapter);
    }
    public void btnChoosePhotosClick() {
        ArrayList<String> selectedItems = ImageAdapter.getCheckedItems();
        if (ImageAdapter.getCheckedItems().size() == 0) {
            count.setText("0");
        } else {
            count.setText(String.valueOf(ImageAdapter.getCheckedItems().size()));
        }
    }
    public class ImageAdapter extends BaseAdapter {
        List<Audio> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;
        String title;
        public ImageAdapter() {
            mSparseBooleanArray = new SparseBooleanArray();
        }
        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();
            for (int i = 0; i < items.length; i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(items[i]);
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
            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
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