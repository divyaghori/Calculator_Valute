package com.example.calculatorhide.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class Util {

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
