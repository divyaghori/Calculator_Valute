package com.example.calculatorhide.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;

import java.util.List;
import java.util.Objects;

class ViewPagerAdapter extends PagerAdapter {

    Context context;

    List<MediaItem> mediaItems ;

    LayoutInflater mLayoutInflater;
    ViewAdapterInterface viewAdapterInterface;
    boolean isPlaying = false;


    public ViewPagerAdapter(Context context, List<MediaItem> items,ViewAdapterInterface viewAdapterInterface) {
        mediaItems = items;
        this.context = context;
        this.viewAdapterInterface = viewAdapterInterface;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface ViewAdapterInterface {
        void onInfoClick(String click);
        void onUnhideClick(String click);
        void onDeleteClick(String click);


    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.activity_imageviewpageritem, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);
        ImageView ivInfo = (ImageView) itemView.findViewById(R.id.ivInfo);
        ImageView ivUnHide = (ImageView) itemView.findViewById(R.id.ivUnHide);
        ImageView ivRecycle = (ImageView) itemView.findViewById(R.id.ivRecycle);
        LinearLayout linear = itemView.findViewById(R.id.linear);
        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapterInterface.onInfoClick(mediaItems.get(position).getPath());

            }
        });
        ivUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapterInterface.onUnhideClick(mediaItems.get(position).getPath());

            }
        });
        ivRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapterInterface.onDeleteClick(mediaItems.get(position).getPath());

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    linear.setVisibility(View.GONE);
                }else{
                    linear.setVisibility(View.VISIBLE);
                }
                isPlaying = !isPlaying;
            }
        });
         Glide.with(context)
                .load(mediaItems.get(position).getPath())
                .into(imageView);
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}