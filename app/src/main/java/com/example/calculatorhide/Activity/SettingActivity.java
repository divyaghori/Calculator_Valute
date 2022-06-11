package com.example.calculatorhide.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculatorhide.BuildConfig;
import com.example.calculatorhide.R;

import java.io.File;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    ImageView back;
    LinearLayout security,important,langugae,blockads,disguiseicon,
            changepassword,rateus,shareapp,recyclebin,update,privacypolicy,about;
    Spinner selectlangugae;
    String[] language = {"English","Hindi"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = findViewById(R.id.back);
        security = findViewById(R.id.security);
        important = findViewById(R.id.important);
        langugae = findViewById(R.id.language);
        blockads = findViewById(R.id.block);
        disguiseicon = findViewById(R.id.disguise);
        changepassword = findViewById(R.id.changepassword);
        rateus = findViewById(R.id.rateus);
        shareapp = findViewById(R.id.share);
        recyclebin = findViewById(R.id.recycler_view);
        update = findViewById(R.id.update);
        privacypolicy = findViewById(R.id.privacy);
        about = findViewById(R.id.about);
        selectlangugae = findViewById(R.id.selectlangugae);
        selectlangugae.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, language);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectlangugae.setAdapter(ad);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,NewPasswordActivity.class);
                startActivity(i);
            }
        });
        important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sunfreeenergy.in/cal"));
                startActivity(browserIntent);
            }
        });
        langugae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blockads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adblcok();
            }
        });
        disguiseicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(SettingActivity.this,DisguiseActivity.class);
                startActivity(browserIntent);
            }
        });
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(SettingActivity.this,DisguiseActivity.class);
                startActivity(browserIntent);
            }
        });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I found this great vault app.");
                String shareMessage= "\nI Found Great App For You, Hide Your Photo, Video & Documents In Calculator :\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            }
        });
        recyclebin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent browserIntent = new Intent(SettingActivity.this,DisguiseActivity.class);
//                startActivity(browserIntent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://calculatorvaulthelp.blogspot.com/2021/02/privacy-policy-body-font-family.html"));
                startActivity(browserIntent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(browserIntent);
            }
        });
    }
    public void adblcok() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.activity_ads_block);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ImageView imageView = dialog.findViewById(R.id.cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(0xdedede);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

}


