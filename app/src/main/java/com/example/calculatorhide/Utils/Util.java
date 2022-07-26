package com.example.calculatorhide.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.calculatorhide.Model.MediaItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {


    public static List<String> activityData_list = new ArrayList<>();
    public static List<MediaItem> tempList = new ArrayList<>();
    public static  File getFolder1() {
        String rootPath = "";
        String path = ".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/"  ;
            file = new File(rootPath);
        } else {
            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" ;
            Log.d("root", rootPath);
            file = new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

}
