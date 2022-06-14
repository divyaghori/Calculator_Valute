package com.example.calculatorhide.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.calculatorhide.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AudioActivity extends AppCompatActivity {
    ActivityResultLauncher<String> storagePermissionLauncher;
    GridView recyclerview;
    GridView gridView;
    SongsAdapter songsAdapter;
    int gridSpanSize = 1;
    SharedViewModel sharedViewModel;
    ImageAdapter imageAdapter;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_audio);
        count = findViewById(R.id.count);

        recyclerview = findViewById(R.id.recyclerview);
        storagePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
           if (result){
               fetchSongs();
           }
           else {
               respondOnUserPermissionActs();
           }
        });
        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void respondOnUserPermissionActs() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            fetchSongs();
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
    private void fetchSongs() {
        List<Song> songs = new ArrayList<>();
        Uri songLibraryUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            songLibraryUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else {
            songLibraryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{
             MediaStore.Audio.Media._ID,
             MediaStore.Audio.Media.DISPLAY_NAME,
             MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.ALBUM_ID,
        };
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";
        try(Cursor cursor = getContentResolver().query(songLibraryUri, projection, null, null,sortOrder)) {
            int idColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int albumIdColumn  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            while (cursor.moveToNext()){
                long id =  cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                long albumId = cursor.getLong(albumIdColumn);
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
                Uri albumartUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);
                name = name.substring(0,name.lastIndexOf("."));
                Song song = new Song(id,uri,name,duration,size,albumId,albumartUri);
                songs.add(song);
            }
            showSongs(songs);
        }
    }

    private void showSongs(List<Song> songs) {
        GridLayoutManager layoutManager = new GridLayoutManager(this,gridSpanSize);
//        recyclerview.setLayoutManager(layoutManager);
        songsAdapter = new SongsAdapter(songs);
        imageAdapter = new ImageAdapter(this, songs);
        recyclerview.setAdapter(imageAdapter);
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
    }
    public void btnChoosePhotosClick() {
        ArrayList<String> selectedItems = ImageAdapter.getCheckedItems();
        if (ImageAdapter.getCheckedItems().size() == 0) {
            count.setText("0");
        } else {
            count.setText(String.valueOf(ImageAdapter.getCheckedItems().size()));
        }
    }
    public static class ImageAdapter extends BaseAdapter {
        static List<Song> mList;
        LayoutInflater mInflater;
        Context mContext;
        static SparseBooleanArray mSparseBooleanArray;
        String title;

        public ImageAdapter(Context context, List<Song> imageList) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<Song>();
            this.mList = imageList;
            this.title = title;
        }

        public static ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();
            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i).getName());
                }
            }
            return mTempArry;
        }

        @Override
        public int getCount() {
            return mList.size();
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
            textView.setText(mList.get(position).getName());

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
        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}