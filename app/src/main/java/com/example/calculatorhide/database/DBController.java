package com.example.calculatorhide.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.calculatorhide.Utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

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
                + columnTime + " text, "
                + columnIsDeleted + " text, "
                + columnFileType + " integer )";
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


}