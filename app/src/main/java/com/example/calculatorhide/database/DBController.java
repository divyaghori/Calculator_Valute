package com.example.calculatorhide.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.room.Query;

import com.example.calculatorhide.Model.MediaItem;
import com.example.calculatorhide.Model.Securityitem;
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
    static final String table = "security";
    static final String columnPassword = "password";
    static final String columnQuestion = "question";
    static final String columnAnswer = "answer";



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
        String security;
        security =  "CREATE TABLE IF NOT EXISTS " + table + "(" + columnId + " integer primary key, "
                + columnPassword + " text, "
                + columnQuestion + " text, "
                + columnAnswer + " text )";
        database.execSQL(security);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS " + tablename;
        database.execSQL(query);
        String security;
        security = "DROP TABLE IF EXISTS " + table;
        database.execSQL(security);
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
//        String selectQuery = "select * from " + tablename + " where " + columnFileType + "='" + type + "'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " +
                        tablename + " where " + columnFileType + " = ? AND " + columnIsDeleted +
                        " = ?",
                new String[] { type, String.valueOf(deleted)});
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
    public MediaItem getFilesByPath(String path){
        String selectQuery = "select * from " + tablename + " where " + columnPath + "='" + path + "'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        MediaItem mediaItem = new MediaItem();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mediaItem.set_id(Integer.parseInt(cursor.getString(0)));
                mediaItem.setName(cursor.getString(1));
                mediaItem.setPath(cursor.getString(2));
                mediaItem.setoPath(cursor.getString(3));
                mediaItem.setFileExt(cursor.getString(4));
                mediaItem.setFolder(cursor.getString(5));
                mediaItem.setTime((cursor.getString(6)));
                mediaItem.setDeleted(Integer.parseInt(cursor.getString(7)));
                mediaItem.setType(cursor.getString(8));

            } while (cursor.moveToNext());
        }
        return mediaItem;
    }
    public void addsecuritydata(Securityitem securityitem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnPassword, securityitem.getPassword());
        values.put(columnQuestion, securityitem.getQuestion());
        values.put(columnAnswer, securityitem.getAnswer());
        db.insert(table, null, values);
        db.close();
    }
    public List<Securityitem> getSecurity() {
        List<Securityitem> securityitems = new ArrayList<Securityitem>();
        String selectQuery = "select * from " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Securityitem securityitem = new Securityitem();
                securityitem.set_id(Integer.parseInt(cursor.getString(0)));
                securityitem.setPassword(cursor.getString(1));
                securityitem.setQuestion(cursor.getString(2));
                securityitem.setAnswer(cursor.getString(3));
                securityitems.add(securityitem);
            } while (cursor.moveToNext());
        }
        return securityitems;
    }
    public void updatepassword(String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnPassword, password);
        db.update(table, values,null,null);
        db.close();
    }

    public void updateQuestionAndAnswer(String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnQuestion, question);
        values.put(columnAnswer, answer);
        db.update(table, values,null,null);
        db.close();
    }

    public Securityitem getqueans(){
        String selectQuery = "select * from " + table;
        SQLiteDatabase db = this.getWritableDatabase();
        Securityitem securityitem = new Securityitem();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                securityitem.set_id(Integer.parseInt(cursor.getString(0)));
                securityitem.setPassword(cursor.getString(1));
                securityitem.setQuestion(cursor.getString(2));
                securityitem.setAnswer(cursor.getString(3));

            } while (cursor.moveToNext());
        }
        return securityitem;
    }
}