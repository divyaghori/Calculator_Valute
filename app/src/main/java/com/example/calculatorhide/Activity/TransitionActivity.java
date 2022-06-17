package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.GoogleAds;
import com.example.calculatorhide.toDoList.MainActivity;

public class TransitionActivity extends AppCompatActivity {



    int index;
    CardView cardview;
    ImageView image;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transtion);
        cardview = findViewById(R.id.cardview);
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        index = getIntent().getIntExtra("index",0);
        if(index == 0){
             cardview.setCardBackgroundColor( Color.rgb(186, 104, 200));
             image.setImageResource(R.drawable.ic_photo_gallery);
             text.setText( SplashActivity.resources.getString(R.string.Gallery));
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,GalleryActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 1){
            cardview.setCardBackgroundColor( Color.rgb(0, 200, 83));
            image.setImageResource(R.drawable.ic_video_editing_app);
            text.setText( SplashActivity.resources.getString(R.string.Video));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,VideoActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 2){
            cardview.setCardBackgroundColor( Color.rgb(0, 137, 123));
            image.setImageResource(R.drawable.ic_headphone);
            text.setText( SplashActivity.resources.getString(R.string.Audio));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,AdioActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 4){
            cardview.setCardBackgroundColor(Color.rgb(0, 176, 255));
            image.setImageResource(R.drawable.ic_documents);
            text.setText( SplashActivity.resources.getString(R.string.Documents));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,DocumentActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 5){
            cardview.setCardBackgroundColor( Color.rgb(255, 145, 0));
            image.setImageResource(R.drawable.ic_folder);
            text.setText( SplashActivity.resources.getString(R.string.File_Manager));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,FilemanagerActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 6){
            cardview.setCardBackgroundColor( Color.rgb(255, 64, 129));
            image.setImageResource(R.drawable.ic_writing);
            text.setText( SplashActivity.resources.getString(R.string.Notes));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 7){
            cardview.setCardBackgroundColor(  Color.rgb(255, 82, 82));
            image.setImageResource(R.drawable.ic_bin);
            text.setText( SplashActivity.resources.getString(R.string.Recycle_Bin));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,RecycleBinActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 8){
            cardview.setCardBackgroundColor( Color.rgb(96, 125, 139));
            image.setImageResource(R.drawable.ic_settings);
            text.setText( SplashActivity.resources.getString(R.string.Settings));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,SettingActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }else if(index == 9){
            cardview.setCardBackgroundColor( Color.rgb(121, 85, 72));
            image.setImageResource(R.drawable.ic_disguise);
            text.setText( SplashActivity.resources.getString(R.string.Disguise_Icon));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(TransitionActivity.this,DisguiseActivity.class);
                    startActivity(i);
                }
            }, 1000);
        }


    }
}