package com.example.calculatorhide.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.calculatorhide.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //member variable
    List<Song> songs;
    //constructor


    public SongsAdapter(List<Song> songs) {
        this.songs = songs;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.song_row_item,parent,false);

        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int pos = position;
        Song song = songs.get(position);
        SongViewHolder viewHolder = (SongViewHolder) holder;

        viewHolder.titleHolder.setText(song.getName());
        viewHolder.sizeHolder.setText(getSize(song.getSize()));
        viewHolder.numberHolder.setText(String.valueOf(position+1));
        viewHolder.durationHolder.setText(getDuration(song.getDuration()));

        Uri albumartUri = song.getAlbumartUri();
        if (albumartUri != null){
            viewHolder.albumartHolder.setImageURI(albumartUri);

            if (viewHolder.albumartHolder.getDrawable() == null){
                viewHolder.albumartHolder.setImageResource(R.drawable.ic_music_disk);
            }
        }
        else {
            viewHolder.albumartHolder.setImageResource(R.drawable.ic_music_disk);
        }

        viewHolder.rowItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }





    @Override
    public int getItemCount() {
        return songs.size();
    }

    //view holder
    public  static  class SongViewHolder extends RecyclerView.ViewHolder{

        //member variables
        ConstraintLayout rowItemLayout;
        ImageView albumartHolder;
        TextView numberHolder, titleHolder,durationHolder,sizeHolder;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            rowItemLayout = itemView.findViewById(R.id.rowItemLayout);
            albumartHolder = itemView.findViewById(R.id.albumart);
            numberHolder = itemView.findViewById(R.id.number);
            titleHolder = itemView.findViewById(R.id.title);
            sizeHolder = itemView.findViewById(R.id.size);
            durationHolder = itemView.findViewById(R.id.duration);
        }
    }

    @SuppressLint("DefaultLocale")
    private String getDuration(int totalDuration){
        String totalDurationText;
            int hrs = totalDuration/(1000*60*60);
            int min = (totalDuration%(1000*60*60))/(1000*60);
            int secs = (((totalDuration%(1000*60*60))%(1000*60*60))%(1000*60))/1000;

            if (hrs<1){ totalDurationText = String.format("%02d:%02d", min, secs); }
            else{
                totalDurationText = String.format("%1d:%02d:%02d", hrs, min, secs);
        }
        return  totalDurationText;
    }

    private  String getSize(long bytes){
        String hrSize;

        double k = bytes/1024.0;
        double m = ((bytes/1024.0)/1024.0);
        double g = (((bytes/1024.0)/1024.0)/1024.0);
        double t = ((((bytes/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format((double) bytes).concat(" Bytes");
        }

        return hrSize;
    }
}
