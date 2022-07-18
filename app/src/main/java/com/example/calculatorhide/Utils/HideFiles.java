package com.example.calculatorhide.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.calculatorhide.Model.HidedDatabase;
import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.Model.SecurityDatabase;
import com.example.calculatorhide.Model.Securityitem;
import com.example.calculatorhide.database.DBController;
import com.github.f4b6a3.uuid.UuidCreator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class HideFiles {
    private List<String> listUris;
    private Context mContext;
    HidedDatabase hidedDatabase;
    DBController dbController;
    SuccessInterface successInterface;
    UUID uuid;
    SecurityDatabase securityDatabase;


    public HideFiles(Context context) {
        mContext = context;
    }

    public void HideFile(List<String> uris, String type, File hiddenPath) {
        hidedDatabase = HidedDatabase.getDatabse(mContext);
        dbController = new DBController(mContext);
        Log.d("databse", hidedDatabase.toString());
        for (int i = 0; i < uris.size(); i++) {
            successInterface.onLoading(true);
            String path = uris.get(i);
            String ex = getFileExtension(path);
            String title = getFileName(path);
            File source = new File(path);
            uuid = UuidCreator.getTimeBased();
            File des = new File(hiddenPath, uuid + "." + "vault");
            MediaItem mediaItem = new MediaItem();
            mediaItem.setType(type);
            mediaItem.setFileExt(ex);
            mediaItem.setName(title);
            mediaItem.setPath(des.getPath());
            mediaItem.setoPath(source.getPath());
            mediaItem.setTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            mediaItem.setDeleted(0);
            mediaItem.setFolder("Default");
            copyFiles(source, des, path);
            dbController.adddata(mediaItem);
        }
        successInterface.onSuccess(true);
    }

    public void unHideFile(List<MediaItem> item) {
        for (int i = 0; i < item.size(); i++) {
            successInterface.onLoading(true);
            hidedDatabase = HidedDatabase.getDatabse(mContext);
            dbController = new DBController(mContext);
            String title = getFileName(item.get(i).getoPath());
            String org = item.get(i).getoPath();
            File src = new File(item.get(i).getPath());
            File des = new File(item.get(i).getoPath());
            String sl = des.getParent();
            File dest = new File(sl, title);
            copyFiles(src, dest, item.get(i).getPath());
            dbController.deletepath(item.get(i).getPath());
//            hidedDatabase.mediaDao().deleteByPath(item.get(i).getPath());
        }
        successInterface.onSuccess(true);
    }
    public void deletemultiplefile(List<MediaItem> item) {
        for (int i = 0; i < item.size(); i++) {
            successInterface.onLoading(true);
            hidedDatabase = HidedDatabase.getDatabse(mContext);
            dbController = new DBController(mContext);
            String title = getFileName(item.get(i).getoPath());
            String org = item.get(i).getoPath();
            File src = new File(item.get(i).getPath());
            File des = new File(item.get(i).getoPath());
            String sl = des.getParent();
            File dest = new File(sl, title);
            copyFiles(src, dest, item.get(i).getPath());
            dbController.addtoRecycle(1, item.get(i).getPath());
        }
        successInterface.onSuccess(true);
    }
    public void restorefile(List<MediaItem> item) {
        for (int i = 0; i < item.size(); i++) {
            successInterface.onLoading(true);
            hidedDatabase = HidedDatabase.getDatabse(mContext);
            dbController = new DBController(mContext);
            String title = getFileName(item.get(i).getoPath());
            String org = item.get(i).getoPath();
            File src = new File(item.get(i).getPath());
            File des = new File(item.get(i).getoPath());
            String sl = des.getParent();
            File dest = new File(sl, title);
            copyFiles(src, dest, item.get(i).getPath());
            dbController.addtoRecycle(1, item.get(i).getPath());
        }
        successInterface.onSuccess(true);
    }
    public String getFileName(String path) {
        File f = new File(path);
        String name = f.getName();
        return name;
    }

    public String getFileExtension(String path) {
        Uri file = Uri.fromFile(new File(path));
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
        return fileExt;
    }

    public void copyFiles(File source, File destination, String path) {
        try {
            FileUtils.copyFile(source, destination);
            delete(mContext, source);
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destination)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");
        contentResolver.delete(filesUri, where, selectionArgs);
        if (file.exists()) {
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    public void getSuccess(SuccessInterface successInterface) {
        this.successInterface = successInterface;
    }

    public interface SuccessInterface {
        void onSuccess(boolean value);

        void onLoading(boolean value);
    }


    public void createSecurity(String password, String question, String answer) {
        securityDatabase = SecurityDatabase.getDatabse(mContext);
        successInterface.onLoading(true);
        Securityitem securityitem = new Securityitem();
        securityitem.setQuestion(question);
        securityitem.setPassword(password);
        securityitem.setAnswer(answer);
        securityDatabase.securityDao().addData(securityitem);
        successInterface.onSuccess(true);
    }

}


