package com.example.calculatorhide.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calculatorhide.Adapter.Home_Adapter;
import com.example.calculatorhide.LOCALIZATION.LocaleHelper;
import com.example.calculatorhide.Model.HomeModel;
import com.example.calculatorhide.R;
import com.example.calculatorhide.toDoList.MainActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    RecyclerView listhome;
    Home_Adapter home_adapter;
    HomeModel[] homeModelList = new HomeModel[]{
            new HomeModel(R.drawable.ic_photo_gallery, SplashActivity.resources.getString(R.string.Gallery), SplashActivity.resources.getString(R.string.Gallery_label), R.drawable.p1, Color.rgb(186, 104, 200)),
            new HomeModel(R.drawable.ic_video_editing_app, SplashActivity.resources.getString(R.string.Video), SplashActivity.resources.getString(R.string.Video_label), R.drawable.pvideo, Color.rgb(0, 200, 83)),
            new HomeModel(R.drawable.ic_headphone, SplashActivity.resources.getString(R.string.Audio), SplashActivity.resources.getString(R.string.Audio_label), R.drawable.paudio, Color.rgb(0, 137, 123)),
            new HomeModel(R.drawable.applock, SplashActivity.resources.getString(R.string.App_Lock), SplashActivity.resources.getString(R.string.App_Lock_label), R.drawable.plock, Color.rgb(103, 58, 183)),
            new HomeModel(R.drawable.ic_documents, SplashActivity.resources.getString(R.string.Documents), SplashActivity.resources.getString(R.string.Documents_label), R.drawable.pdocument, Color.rgb(0, 176, 255)),
            new HomeModel(R.drawable.ic_folder, SplashActivity.resources.getString(R.string.File_Manager), SplashActivity.resources.getString(R.string.File_Manager_label), R.drawable.pfilemanager, Color.rgb(255, 145, 0)),
            new HomeModel(R.drawable.ic_writing, SplashActivity.resources.getString(R.string.Notes), SplashActivity.resources.getString(R.string.Notes_label), R.drawable.pnote, Color.rgb(255, 64, 129)),
            new HomeModel(R.drawable.ic_bin, SplashActivity.resources.getString(R.string.Recycle_Bin), SplashActivity.resources.getString(R.string.Recycle_Bin_label), R.drawable.precycle, Color.rgb(255, 82, 82)),
            new HomeModel(R.drawable.ic_settings, SplashActivity.resources.getString(R.string.Settings), SplashActivity.resources.getString(R.string.Settings_label), R.drawable.psetting, Color.rgb(96, 125, 139)),
            new HomeModel(R.drawable.ic_disguise, SplashActivity.resources.getString(R.string.Disguise_Icon), SplashActivity.resources.getString(R.string.Disguise_Icon_label), R.drawable.picon, Color.rgb(121, 85, 72))
    };
    ImageView more;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static final int PERMISSION_REQUEST_CODE = 100;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkMultiplePermissions();
//        if(checkPermission()){
            findID();
//        }else{
//            requestPermission();
//        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void findID() {
        more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });
        listhome = findViewById(R.id.listhome);
        home_adapter = new Home_Adapter(getApplicationContext(), homeModelList, new Home_Adapter.HomeAdapterInterface() {
            @Override
            public void onRowClick(int click) {
                if (click == 0) {
                    Intent i = new Intent(HomeActivity.this, GalleryActivity.class);
                    startActivity(i);
                }
                if (click == 1) {
                    Intent i = new Intent(HomeActivity.this, VideoActivity.class);
                    startActivity(i);
                }
                if (click == 2) {
                    Intent i = new Intent(HomeActivity.this, AdioActivity.class);
                    startActivity(i);
                }
                if (click == 4) {
                    Intent i = new Intent(HomeActivity.this, DocumentActivity.class);
                    startActivity(i);
                }
                if (click == 5) {
                    Intent i = new Intent(HomeActivity.this, FilemanagerActivity.class);
                    startActivity(i);
                }
                if (click == 8) {
                    Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(i);
                }
                if (click == 6) {
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                }
                if (click == 9) {
                    Intent i = new Intent(HomeActivity.this, DisguiseActivity.class);
                    startActivity(i);
                }

            }
        });
        listhome.setLayoutManager(new GridLayoutManager(this, 2));
        listhome.setAdapter(home_adapter);
    }

//    private boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        } else {
//            return false;
//        }
//    }
private boolean checkPermission() {
    if (SDK_INT >= Build.VERSION_CODES.R) {
        return Environment.isExternalStorageManager();
    } else {
        int result = ContextCompat.checkSelfPermission(HomeActivity.this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(HomeActivity.this, WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
}
    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    } else {
                        Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    //    private void requestPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            Toast.makeText(HomeActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Log.e("value", "Permission Granted, Now you can use local drive .");
//            } else{
//                Log.e("value", "Permission Denied, You cannot use local drive .");
//            }
//            break;
//        }
//    }
    private void checkMultiplePermissions() {

        if (SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Write Storage");
            }
            if (!addPermission(permissionsList, READ_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Read Storage");
            }
//            if (!addPermission(permissionsList, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
//                permissionsNeeded.add("manage external  Storage");
//            }
            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (SDK_INT >= 23)
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<String, Integer>();
//                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
////                perms.put(Manifest.permission.MANAGE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                for (int i = 0; i < permissions.length; i++)
//                    perms.put(permissions[i], grantResults[i]);
//                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
////                        perms.get(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    return;
//                } else {
//                    if (SDK_INT >= 23) {
//                        Toast.makeText(
//                                getApplicationContext(),
//                                "My App cannot run without Location and Storage " +
//                                        "Permissions.\nRelaunch My App or allow permissions" +
//                                        " in Applications Settings",
//                                Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                }
//            }
//            break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
}


