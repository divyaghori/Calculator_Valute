package com.example.calculatorhide.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;

import java.util.List;


public class RecycleBinAdapter extends ArrayAdapter<MediaItem> {
    private Context mContext;
    private RecyclerViewItemInterface viewItemInterface;
    public RecycleBinAdapter(@NonNull Context context, List<MediaItem> mediaItems) {
        super(context, 0, mediaItems);
        mContext=context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MediaItem data=getItem(position);
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.recycle_ticket, parent, false);
        }
        ImageView ivIcon = listitemView.findViewById(R.id.ivItem);
        TextView tvTitle=listitemView.findViewById(R.id.tvTitle);
        if(data.getType().equalsIgnoreCase("video")) {
            Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(data.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);

            Glide.with(mContext)
                    .load(bitmap2)
                    .centerCrop()
                    .into(ivIcon);
        }
        if(data.getType().equalsIgnoreCase("HiddenFileType.image")) {
            Glide.with(mContext)
                    .load(data.getPath())
                    .centerCrop()
                    .into(ivIcon);
        }
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewItemInterface != null) {
                    viewItemInterface.onItemClick(position,data);
                }
            }
        });
//        Uri file = Uri.fromFile(new File(data.getDest_uri()));
//        String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        String filename=data.getPath().substring(data.getPath().lastIndexOf("/")+1);
        tvTitle.setText(filename);
        return listitemView;
    }
    public void setViewItemInterface(RecyclerViewItemInterface viewItemInterface) {
        this.viewItemInterface = viewItemInterface;
    }
    public interface RecyclerViewItemInterface {

        void onItemClick(int position, MediaItem data);

    }
}