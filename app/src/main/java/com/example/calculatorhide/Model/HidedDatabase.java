package com.example.calculatorhide.Model;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calculatorhide.Utils.Util;
@Database(entities={MediaItem.class},version = 1)
public abstract class HidedDatabase extends RoomDatabase {
    public static HidedDatabase getDatabse(Context context)
    {
        HidedDatabase database;
        Util util=new Util();
        database= Room.databaseBuilder(context, HidedDatabase.class,
                util.getFolder(context)+"/hideFiles").allowMainThreadQueries().build();
        return database;
    }
    public abstract MediaDao mediaDao();
}
//allowMainThreadQueries().fallbackToDestructiveMigration().