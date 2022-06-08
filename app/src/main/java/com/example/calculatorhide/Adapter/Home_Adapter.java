package com.example.calculatorhide.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.example.calculatorhide.Model.HomeModel;
import com.example.calculatorhide.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.ViewHolder> {

    private HomeModel[] listdata;
    HomeAdapterInterface myListAdapterInterface;
    Context context;

    public interface HomeAdapterInterface {
        void onRowClick(int click);

    }

    public Home_Adapter(Context context,HomeModel[] listdata, HomeAdapterInterface myListAdapterInterface) {
        this.listdata = listdata;
        this.context = context;
        this.myListAdapterInterface = myListAdapterInterface;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.activity_home_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(listdata[position].getName());
        holder.subname.setText(listdata[position].getSubname());
        holder.image.setImageResource(listdata[position].getImage());
        holder.progessimage.setBackground(context.getDrawable(listdata[position].getProgrssimage()));
        holder.card.setCardBackgroundColor(listdata[position].getColor());
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListAdapterInterface.onRowClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        View progessimage;
        public TextView name, subname;
        public LinearLayout linear;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.subname = (TextView) itemView.findViewById(R.id.subname);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.progessimage = (View) itemView.findViewById(R.id.progrssimage);
            linear = itemView.findViewById(R.id.linear);
            card = itemView.findViewById(R.id.cardview);

        }
    }
}
