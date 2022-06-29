package com.example.calculatorhide.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

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


    public File getFolder(Context context)
    {
        String rootPath="";
        String path=".CalculatorVault";
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rootPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+path;
            file= new File(rootPath);
        } else {
            rootPath=context.getExternalFilesDir(null).getAbsoluteFile()+"/"+path;
            file= new File(rootPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
