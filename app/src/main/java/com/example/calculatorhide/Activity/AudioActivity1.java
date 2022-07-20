package com.example.calculatorhide.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.CustomProgressDialogue;
import com.example.calculatorhide.Utils.HideFiles;
import com.example.calculatorhide.database.DBController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AudioActivity1 extends AppCompatActivity {
   
    Activity activity;
    ImageView icback,getimage;
    GridView gvGallery;
    TextView tvNoData,maintext;
    HideFiles hideFiles;
    CustomProgressDialogue dialogue;
    private static final int REQ_CODE=123;
    private List<String> file_uris=new ArrayList<>();
    private List<MediaItem>mediaItems=new ArrayList<>();
    private AudioAdapter adapter;
    ImageView image;
    AdView mAdView;
    CheckBox check;
    private List<MediaItem> selectedItems = new ArrayList<>();
    ImageView unlock,delete;
    boolean checked = false;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        activity=this;
        db = new DBController(this);
        findId();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void findId() {
        hideFiles = new HideFiles(activity);
        dialogue=new CustomProgressDialogue(activity);
        icback = findViewById(R.id.back);
        gvGallery=findViewById(R.id.gvGallery);
        tvNoData=findViewById(R.id.tvNodata);
        maintext = findViewById(R.id.maintext);
        maintext.setText(SplashActivity.resources.getString(R.string.Audio));
        tvNoData.setText(SplashActivity.resources.getString(R.string.No_files_added));
        image = findViewById(R.id.image);
        check = findViewById(R.id.check);
        unlock = findViewById(R.id.unlock);
        delete = findViewById(R.id.delete);

        icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getimage = findViewById(R.id.getimage);
        getimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,ImportAudioActivity.class);
                startActivityForResult(i,123);
            }
        });
        hideFiles.getSuccess(new HideFiles.SuccessInterface() {
            @Override
            public void onSuccess(boolean value) {
                getAudio();
                dialogue.dismiss();

            }

            @Override
            public void onLoading(boolean value) {
                dialogue.show();
            }
        });
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnChoosePhotosClick();
                        checked = check.isChecked();
                    }
                });
            }
        }, 1000, 1000);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnHideRcyclePopup1(adapter.getCheckedItems());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRcyclePopup1(adapter.getCheckedItems());
            }
        });
    }
    public void btnChoosePhotosClick() {
        if(selectedItems.size()!=0) {
            selectedItems = adapter.getCheckedItems();
            if (selectedItems.size() == 0) {
                unlock.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            } else {
                unlock.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        }
        Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
    }
    private void getAudio() {
        mediaItems.clear();
        mediaItems = db.getmedia("audio", 0);
        if(mediaItems.size()!=0) {
            tvNoData.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            adapter = new AudioAdapter(activity, mediaItems, checked,check.isChecked());
            gvGallery.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    adapter.OnSelectionModeChange(isChecked);
                }
            });
        }
        else
        {
            tvNoData.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }

    }

    public File getFolder() {
        String rootPath = "";
        String path = ".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files" ;
            file = new File(rootPath);
        } else {
            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" + "files";
//            rootPath = getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
            Log.d("root", rootPath);
            file = new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE)
        {
            if(data!=null) {
                file_uris = (List<String>) data.getSerializableExtra("files");
                hideFiles.HideFile(file_uris, "audio", getFolder());
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_hide_progress, null);
                dialogBuilder.setView(dialogView);
                TextView done = dialogView.findViewById(R.id.done);
                AlertDialog alertDialog = dialogBuilder.create();
                Handler h1 = new Handler();
                h1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        done.setVisibility(View.VISIBLE);
                    }
                }, 2500);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAudio();
                    }
                },1000);

            }

        }
    }

    @Override
    protected void onResume() {
        getAudio();
        super.onResume();

    }
    public void showUnHideRcyclePopup(int position, MediaItem item)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide =dialogView.findViewById(R.id.tvUnHide);
        TextView tvRecycle =dialogView.findViewById(R.id.tvRecycleBin);
        //
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MediaItem>itemList=new ArrayList<>();
                itemList.add(item);
                hideFiles.unHideFile(itemList);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addtoRecycle(1,item.getPath());
                getAudio();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }
    public void showUnHideRcyclePopup1( List<MediaItem> itemList) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        maintext.setText(SplashActivity.resources.getString(R.string.unhideSelected));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.unhide));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.Recycle_Bin));
        AlertDialog alertDialog = dialogBuilder.create();
        tvUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<MediaItem> itemList = new ArrayList<>();
//                itemList.add(item);
//                hideFiles.deletemultiplefile(itemList);
                hideFiles.unHideFile(itemList);
                unlock.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });
        tvRecycle.setVisibility(View.GONE);
        alertDialog.show();
    }
    public void showRcyclePopup1( List<MediaItem> itemList) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.long_ubhide_popup, null);
        dialogBuilder.setView(dialogView);
        TextView tvUnHide = dialogView.findViewById(R.id.tvUnHide);
        TextView maintext = dialogView.findViewById(R.id.maintext);
        TextView tvRecycle = dialogView.findViewById(R.id.tvRecycleBin);
        maintext.setText(SplashActivity.resources.getString(R.string.recycleSelected));
        tvUnHide.setText(SplashActivity.resources.getString(R.string.unhide));
        tvRecycle.setText(SplashActivity.resources.getString(R.string.Recycle_Bin));
        AlertDialog alertDialog = dialogBuilder.create();
        tvRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<MediaItem> itemList = new ArrayList<>();
//                itemList.add(item);
                hideFiles.deletemultiplefile(itemList);
                unlock.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });
        tvUnHide.setVisibility(View.GONE);
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AudioActivity1.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public class AudioAdapter extends ArrayAdapter<MediaItem> {
        private Context mContext;
        private List<MediaItem>mitemList;
        boolean cheked;
        SparseBooleanArray mSparseBooleanArray;
        private Boolean selectionMode;
        public void OnSelectionModeChange(Boolean selectionMode){
            this.selectionMode = selectionMode;
            notifyDataSetChanged();
        }
        public AudioAdapter(Context context, List<MediaItem>itemList, boolean checked,Boolean selectionMode) {
            super(context,0,itemList);
            mContext=context;
            this.selectionMode= selectionMode;
            mitemList=itemList;
            this.cheked = checked;
            mSparseBooleanArray = new SparseBooleanArray();
        }
        public ArrayList<MediaItem> getCheckedItems() {
            ArrayList<MediaItem> mTempArry = new ArrayList<MediaItem>();
            for (int i = 0; i < mitemList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mitemList.get(i));
                }
            }
            return mTempArry;
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
            CheckBox mCheckBox = view.findViewById(R.id.checkbox);
            mCheckBox.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
            if(item!=null) {
                if (item.getType().equalsIgnoreCase("audio")) {
                    ivItem.setText(item.getName());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(activity,AudioViewActivity.class);
                            intent.putExtra("path",mediaItems.get(position).getPath());
                            intent.putExtra("name",mediaItems.get(position).getName());
                            startActivity(intent);
                        }
                    });
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            showUnHideRcyclePopup(position, item);
                            return false;
                        }
                    });
                }

            }

            return view;

        }
        CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                }else {
                    mSparseBooleanArray.removeAt(mSparseBooleanArray.indexOfKey((Integer) buttonView.getTag()));
                }
                unlock.setVisibility(mSparseBooleanArray.size() > 0 ? View.VISIBLE :View.GONE);
                delete.setVisibility(mSparseBooleanArray.size() > 0 ? View.VISIBLE :View.GONE);

            }
        };

    }

}