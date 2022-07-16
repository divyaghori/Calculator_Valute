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

    // Context object
    Context context;

    // Array of images
   // int[] images;
    List<MediaItem> mediaItems ;

    // Layout Inflater
    LayoutInflater mLayoutInflater;
    // ama list joise ne? haa


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<MediaItem> items) {
        mediaItems = items;
        this.context = context;
        //this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return mediaItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.activity_imageviewpageritem, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        // setting the image in the imageView
        // path kyathi avsr?
        Glide.with(context)
                .load(mediaItems.get(position).getPath())
                .into(imageView);
        //imageView.set(mediaItems.get(position).getPath());

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}