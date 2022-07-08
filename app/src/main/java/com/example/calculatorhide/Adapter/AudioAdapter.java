package com.example.calculatorhide.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;

import java.util.List;

public class AudioAdapter extends ArrayAdapter<MediaItem> {
    private Context mContext;
    private List<MediaItem>mitemList;
    private ClickItemInterface clickItemInterface;
    public AudioAdapter(Context context, List<MediaItem>itemList) {
        super(context,0,itemList);
        mContext=context;
        mitemList=itemList;
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
        MediaItem item=mitemList.get(position);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audio_item, parent, false);
            TextView ivItem = view.findViewById(R.id.ivItem);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
           if(item!=null) {
              if (item.getType().equalsIgnoreCase("audio")) {
                 ivItem.setText(item.getName());
//                 Glide.with(mContext)
//                        .load(item.getPath())
//                        .centerCrop()
//                        .into(ivItem);
                 view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         if(clickItemInterface!=null)
                         {
                            clickItemInterface.onItemClick(position,mitemList.get(position).getPath());
                         }
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(clickItemInterface!=null)
                        {
                            clickItemInterface.onItemLongClick(position,item);
                        }
                        return false;
                    }
                });
            }

        }

        return view;

    }
    public void setClickItemInterface(ClickItemInterface clickItemInterface)
    {
        this.clickItemInterface=clickItemInterface;
    }
    public interface ClickItemInterface {
        void onItemClick(int position, String path);
        void onItemLongClick(int position, MediaItem item);
    }
}
