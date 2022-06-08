package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.R;

import java.util.ArrayList;

public class Importphoto extends Activity {
    private ArrayList<String> images;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    private int count;

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
//                if (null != images && !images.isEmpty())
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "position " + position + " " + images.get(position),
//                            300).show();
            }
        });
    }
    private class ImageAdapter extends BaseAdapter {
        private Activity context;
        private LayoutInflater mInflater;

        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
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
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.activity_import_item_adapter, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                holder.frame = convertView.findViewById(R.id.frame);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.checkbox.setVisibility(View.VISIBLE);
                }
            });
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
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

        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);
            count = cursor.getCount();
            thumbnails = new Bitmap[count];
            arrPath = new String[count];
            thumbnailsselection = new boolean[count];
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int id = cursor.getInt(column_index_data);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(), id,
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
                arrPath[i]= cursor.getString(dataColumnIndex);
            }
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
        FrameLayout frame;
    }
}
