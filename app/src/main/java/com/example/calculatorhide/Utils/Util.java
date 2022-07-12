package com.example.calculatorhide.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {

    // public static int CurrentScreen;

    // 0 Spalsj
    // 1 Calculator
    //2 Home
    // 3 TransitionActivity
    // 4
    public static List<String> activityData_list = new ArrayList<>();

//    public File getFolder(Context context) {
//        String rootPath = "";
//        String path = ".CalculatorVault";
//        File file = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////            file = new File(Environment.getExternalStorageDirectory(), path);
//            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android") + "/"
//                    + path + "/" + "files";
//            file = new File(rootPath);
//        } else {
////            file = new File(Environment.getExternalStorageDirectory(), path);
//            rootPath = context.getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
//            Log.d("root", rootPath);
//            file = new File(rootPath);
//        }
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return file;
//    }
//    public File getFolder(Context context)
//    {
//        String rootPath="";
//        String path=".CalculatorVault";
//        File file = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            rootPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+path;
//            file= new File(rootPath);
//        } else {
//            rootPath=context.getExternalFilesDir(null).getAbsoluteFile()+"/"+path;
//            file= new File(rootPath);
//        }
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        return file;

    public File getFolder(Context context) {
        String rootPath = "";
        String path = "CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/"  ;
            file = new File(rootPath);
        } else {
            file = new File(Environment.getExternalStorageDirectory(), path);
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().split("Android")[0] + "/"
                    + path + "/" ;
//            rootPath = getExternalFilesDir(null).getAbsoluteFile() + "/" + path + "/" + "files";
            Log.d("root", rootPath);
            file = new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
