package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.R;

import java.util.ArrayList;
import java.util.List;

public class ImportAudio extends Activity {

    private List<String> images;
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_photo);
        GridView gallery = (GridView) findViewById(R.id.PhoneImageGrid);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(getApplicationContext(),
                            "position " + position + " " +
                                    images.get(position), 300).show();
                Intent i = new Intent(ImportAudio.this, VideoPlayerActivity.class);
                i.putExtra("getvideo", images.get(position));
                startActivity(i);
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {
        private Activity context;
        private LayoutInflater mInflater;

        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllAudioFromDevice(context);
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
//            ImageView picturesView;
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.activity_import_item_adapter, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
//                picturesView = new ImageView(context);
//                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                picturesView
//                        .setLayoutParams(new GridView.LayoutParams(270, 270));
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
                    .into(holder.imageview);
            holder.checkbox.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }

        public List<String> getAllAudioFromDevice(final Context context) {
            final List<String> tempAudioList = new ArrayList<>();
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Audio.AudioColumns.DATA,
                    MediaStore.Audio.Media.TITLE,
            };
            Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);
            column_index_data = c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (c.moveToNext()) {
                absolutePathOfImage = c.getString(column_index_data);

                tempAudioList.add(absolutePathOfImage);
            }
//            if (c != null) {
//                while (c.moveToNext()) {
//                    absolutePathOfImage = c.getString(column_index_data);
//                    tempAudioList.add(absolutePathOfImage);
//                }
//                c.close();
//            }
            return tempAudioList;
        }

        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Audio.Media.TITLE};

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);
            count = cursor.getCount();
            thumbnails = new Bitmap[count];
            arrPath = new String[count];
            thumbnailsselection = new boolean[count];
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
//            for (int i = 0; i < count; i++) {
//                cursor.moveToPosition(i);
//                int id = cursor.getInt(column_index_data);
//                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
//                        getApplicationContext().getContentResolver(), id,
//                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
//                arrPath[i]= cursor.getString(dataColumnIndex);
//            }
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }

    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}
