package com.example.calculatorhide.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
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


    public ViewPagerAdapter(Context context, List<MediaItem> items) {
        mediaItems = items;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.activity_imageviewpageritem, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        Glide.with(context)
                .load(mediaItems.get(position).getPath())
                .into(imageView);
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}