package com.example.calculatorhide.Model;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calculatorhide.Utils.Util;

@Database(entities={Securityitem.class},version = 1)
public abstract class SecurityDatabase extends RoomDatabase {
    public static SecurityDatabase getDatabse(Context context)
    {
        SecurityDatabase database;
        Util util=new Util();
        database= Room.databaseBuilder(context, SecurityDatabase.class,
                util.securitygetFolder(context)+"/hideFiles").allowMainThreadQueries().build();
        return database;
    }
    public abstract SecurityDao securityDao();
}
//allowMainThreadQueries().fallbackToDestructiveMigration().