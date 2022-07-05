package com.example.calculatorhide.Activity;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.example.calculatorhide.Adapter.Home_Adapter;
import com.example.calculatorhide.Model.HomeModel;
import com.example.calculatorhide.R;
import com.example.calculatorhide.Utils.ActivityData;
import com.example.calculatorhide.Utils.InterstitialAdManager;
import com.example.calculatorhide.Utils.Util;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeActivity extends AppCompatActivity  implements IAPHelper.IAPHelperListener{

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    AtomicBoolean atomicBooleanGallary = new AtomicBoolean();
    RecyclerView listhome;
    Home_Adapter home_adapter;
    HomeModel[] homeModelList = new HomeModel[]{
            new HomeModel(R.drawable.ic_photo_gallery, "Gallery", "Hidden Gallery", R.drawable.p1, Color.rgb(186, 104, 200)),
            new HomeModel(R.drawable.ic_video_editing_app, "Video", "Hidden Videos", R.drawable.pvideo, Color.rgb(0, 200, 83)),
            new HomeModel(R.drawable.ic_headphone, "Audio", "Hidden Audios", R.drawable.paudio, Color.rgb(0, 137, 123)),
            new HomeModel(R.drawable.applock, "App Lock", "Hide Apps", R.drawable.plock, Color.rgb(103, 58, 183)),
            new HomeModel(R.drawable.ic_documents, "Documents", "Hidden Document", R.drawable.pdocument, Color.rgb(0, 176, 255)),
            new HomeModel(R.drawable.ic_folder, "File Manager", "Categorised Docs", R.drawable.pfilemanager, Color.rgb(255, 145, 0)),
            new HomeModel(R.drawable.ic_writing, "Notes", "Your Notes", R.drawable.pnote, Color.rgb(255, 64, 129)),
            new HomeModel(R.drawable.ic_bin, "Recycle Bin", "Deleted Hidden Files", R.drawable.precycle, Color.rgb(255, 82, 82)),
            new HomeModel(R.drawable.ic_settings, "Settings", "Application Settings", R.drawable.psetting, Color.rgb(96, 125, 139)),
            new HomeModel(R.drawable.ic_disguise, "Disguise Icon", "App Icon", R.drawable.picon, Color.rgb(121, 85, 72))
    };
    ImageView more;
    Activity activity;

    private Context context = this;
    private InterstitialAdManager manager;
    FloatingActionButton gallery, fabvideo, fabapplock, addfolder, howtouse;
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
    IAPHelper iapHelper;
    final String TEST = "android.test.purchased"; //This id can be used for testing purpose
    private List<String> skuList = Arrays.asList(TEST);
    private boolean isAdShowen;
    boolean isFromCalculatorActivity;
    ImageView noad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (SDK_INT >= 23) {
            checkMultiplePermissions();
        }
        noad = findViewById(R.id.noad);
        noad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch(TEST);
            }
        });
        iapHelper = new IAPHelper(this, this, skuList);
        // if(Util.activityData_list.)
        if (Util.activityData_list.contains("HomeActivity")) {
            isAdShowen = false;
        } else {
            isAdShowen = true;
            Util.activityData_list.add("HomeActivity");
        }

        isFromCalculatorActivity = getIntent().getBooleanExtra("start", true);
        if (!isFromCalculatorActivity)
            setAdAtomic();
        findID();
        activity = this;
        fabvideo = findViewById(R.id.video);
        fabapplock = findViewById(R.id.applock);
        addfolder = findViewById(R.id.addfolder);
        howtouse = findViewById(R.id.howtouse);
        gallery = findViewById(R.id.fabgallery);
        fabvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, VideoActivity.class);
                startActivity(i);
            }
        });
        fabapplock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sunfreeenergy.in/applocker/"));
                startActivity(browserIntent);
            }
        });
        addfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, GalleryActivity.class);
                startActivity(i);
            }
        });
        howtouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sunfreeenergy.in/cal"));
                startActivity(browserIntent);
            }
        });
    }

    private void launch(String sku){
        if(!skuDetailsHashMap.isEmpty())
            iapHelper.launchBillingFLow(skuDetailsHashMap.get(sku));
    }
    private void setAdAtomic() {
        atomicBooleanGallary.set(true);
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

        manager = new InterstitialAdManager();
        manager.fetchAd(this, false);
        home_adapter = new Home_Adapter(getApplicationContext(), homeModelList, new Home_Adapter.HomeAdapterInterface() {
            @Override
            public void onRowClick(int click) {
                if (click == 3) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sunfreeenergy.in/applocker/"));
                    startActivity(browserIntent);
                    return;
                }
                //if (click == 0) {
                // if (atomicBooleanGallary.get()) {
//                    if(!isAdShowen){
//                        isAdShowen = true;
                if (isAdShowen) {
                   // atomicBooleanGallary.getAndSet(false);
                    InterstitialAd interstitialAd = manager.showIfItAvaible();
                    if (interstitialAd != null) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                Intent i = new Intent(HomeActivity.this, TransitionActivity.class);


                                i.putExtra("index", click);
                                startActivity(i);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
                                i.putExtra("index", click);
                                startActivity(i);
                            }
                        });
                        interstitialAd.show(HomeActivity.this);
                    }
                } else {
                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
                    i.putExtra("index", click);
                    startActivity(i);
                }
                //               }
//                if (click == 1) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 1);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 1);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                            Toast.makeText(activity, "In If", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 1);
//                            Toast.makeText(activity, "In Else", Toast.LENGTH_SHORT).show();
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 1);
//                        startActivity(i);
//                    }
//                }
//                if (click == 2) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 2);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 2);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 2);
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 2);
//                        startActivity(i);
//                    }
//                }
//                if (click == 4) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 4);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 4);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 4);
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 4);
//                        startActivity(i);
//                    }
//                }
//                if (click == 5) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 5);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 5);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 5);
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 5);
//                        startActivity(i);
//                    }
//                }
//                if (click == 8) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 8);
//
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 8);
//
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 8);
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 8);
//                        startActivity(i);
//                    }
//                }
//                if (click == 6) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 6);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 6);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 6);
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 6);
//                        startActivity(i);
//                    }
////                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
////                    startActivity(i);
//                }
//                if (click == 7) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 7);
//                                    startActivity(i);
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 7);
//                                    startActivity(i);
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 7);
//
//                            startActivity(i);
//                        }
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 7);
//
//                        startActivity(i);
//                    }
//                }

//                if (click == 9) {
//                    if (atomicBooleanGallary.get()) {
//                        atomicBooleanGallary.getAndSet(false);
//                        InterstitialAd interstitialAd = manager.showIfItAvaible();
//                        if (interstitialAd != null) {
//                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                                @Override
//                                public void onAdDismissedFullScreenContent() {
//                                    super.onAdDismissedFullScreenContent();
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 9);
//
//                                    startActivity(i);
//
//                                }
//
//                                @Override
//                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                    super.onAdFailedToShowFullScreenContent(adError);
//                                    Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                                    i.putExtra("index", 9);
//
//                                    startActivity(i);
//
//                                }
//                            });
//                            interstitialAd.show(HomeActivity.this);
//                        } else {
//                            Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                            i.putExtra("index", 9);
//
//                            startActivity(i);
//
//                        }
//
//                    } else {
//                        Intent i = new Intent(HomeActivity.this, TransitionActivity.class);
//                        i.putExtra("index", 9);
//                        startActivity(i);
//
//                    }

                //               }

            }
        });
        listhome.setLayoutManager(new GridLayoutManager(this, 2));
        listhome.setAdapter(home_adapter);
    }

    private void checkMultiplePermissions() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createAppFolder();

            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        if (SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Write Storage");
            }

            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Read Storage");
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    createAppFolder();
                    return;
                } else {
                    if (SDK_INT >= 23) {
                        Toast.makeText(
                                getApplicationContext(),
                                "My App cannot run without Location and Storage " +
                                        "Permissions.\nRelaunch My App or allow permissions" +
                                        " in Applications Settings",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void createAppFolder() {
//        String rootPath = "";
//        String path = ".CalculatorVault";
//        File file = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path + "/" + "files";
//            file = new File(rootPath);
//        } else {
//            rootPath = context.getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
//            file = new File(rootPath);
//        }
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String rootPath = "";
//        String path = ".CalculatorVault";
//        File file = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////            file = new File(Environment.getExternalStorageDirectory(), path);
//            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
//                    + path + "/" + "files" ;
//            file = new File(rootPath);
//        } else {
//            file = new File(Environment.getExternalStorageDirectory(), path);
//            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
//                    + path + "/" + "files";
////            rootPath = getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
//            Log.d("root", rootPath);
//            file = new File(rootPath);
//        }
//        if (!file.exists()) {
//            file.mkdirs();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // manager = new InterstitialAdManager();
        // manager.fetchAd(this, false);
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createAppFolder();
            }
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onSkuListResponse(HashMap<String, SkuDetails> skuDetails) {
        skuDetailsHashMap = skuDetails;
    }

    @Override
    public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {
        if (purchasedItems != null) {

        }
    }

    @Override
    public void onPurchaseCompleted(Purchase purchase) {
        Toast.makeText(getApplicationContext(), "Purchase Successful", Toast.LENGTH_SHORT).show();
        updatePurchase(purchase);
    }

    private void updatePurchase(Purchase purchase){
        String sku = purchase.getSku();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapHelper != null)
            iapHelper.endConnection();
    }
}


