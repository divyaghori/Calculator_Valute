package com.example.calculatorhide.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.Utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBController extends SQLiteOpenHelper {
    static final String tablename = "hidden_file";
    static final String columnId = "_id";
    static final String columnName = "name";
    static final String columnPath = "path";
    static final String columnOriginalPath = "oPath";
    static final String columnFileExtension = "fileExt";
    static final String columnFolder = "folder";
    static final String columnTime = "time";
    static final String columnIsDeleted = "deleted";
    static final String columnFileType = "type";
     static final String databasename = Util.getFolder1()+"/hideFiles";
     static final int versioncode = 1;

    public DBController(Context context) {
        super(context, databasename, null, versioncode);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS " + tablename + "(" + columnId + " integer primary key, "
                + columnName + " text, "
                + columnPath + " text, "
                + columnOriginalPath + " text, "
                + columnFileExtension + " text, "
                + columnFolder + " text, "
                + columnTime + " integer, "
                + columnIsDeleted + " integer,"
                + columnFileType + " text )";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + tablename;
        database.execSQL(query);
        onCreate(database);
    }
    public void adddata(MediaItem mediaItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnName, mediaItem.getName());
        values.put(columnPath, mediaItem.getPath());
        values.put(columnOriginalPath, mediaItem.getoPath());
        values.put(columnFileExtension, mediaItem.getFileExt());
        values.put(columnFolder, mediaItem.getFolder());
        values.put(columnTime, mediaItem.getTime());
        values.put(columnIsDeleted, mediaItem.getDeleted());
        values.put(columnFileType, mediaItem.getType());
        db.insert(tablename, null, values);
        db.close();
    }
    public List<MediaItem> getmedia(String type,int deleted) {
        List<MediaItem> mediaItems = new ArrayList<MediaItem>();
        String selectQuery = "select * from " + tablename + " where " + columnFileType + "='" + type + "'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MediaItem mediaItem = new MediaItem();
                mediaItem.set_id(Integer.parseInt(cursor.getString(0)));
                mediaItem.setName(cursor.getString(1));
                mediaItem.setPath(cursor.getString(2));
                mediaItem.setoPath(cursor.getString(3));
                mediaItem.setFileExt(cursor.getString(4));
                mediaItem.setFolder(cursor.getString(5));
                mediaItem.setTime((cursor.getString(6)));
                mediaItem.setDeleted(Integer.parseInt(cursor.getString(7)));
                mediaItem.setType(cursor.getString(8));

                mediaItems.add(mediaItem);
            } while (cursor.moveToNext());
        }
        return mediaItems;
    }
    public void deletepath(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename, columnPath + " = ?",
                new String[] { String.valueOf(path) });
        db.close();
    }

    public void addtoRecycle(int value,String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnIsDeleted, value);
        db.update(tablename, values, columnPath + " = ?",
                new String[] { String.valueOf(path) });
        db.close();
    }
    public List<MediaItem> getRecycleData(int deleted) {
        List<MediaItem> mediaItems = new ArrayList<MediaItem>();
        String selectQuery = "select * from " + tablename + " where " + columnIsDeleted + "='" + deleted + "'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                MediaItem mediaItem = new MediaItem();
                mediaItem.set_id(Integer.parseInt(cursor.getString(0)));
                mediaItem.setName(cursor.getString(1));
                mediaItem.setPath(cursor.getString(2));
                mediaItem.setoPath(cursor.getString(3));
                mediaItem.setFileExt(cursor.getString(4));
                mediaItem.setFolder(cursor.getString(5));
                mediaItem.setTime((cursor.getString(6)));
                mediaItem.setDeleted(Integer.parseInt(cursor.getString(7)));
                mediaItem.setType(cursor.getString(8));
                mediaItems.add(mediaItem);
            } while (cursor.moveToNext());
        }
        return mediaItems;
    }

}