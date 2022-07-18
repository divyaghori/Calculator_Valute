package com.example.calculatorhide.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "hidden_file")
public class MediaItem1 implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "oPath")
    public String oPath;

    @ColumnInfo(name = "fileExt")
    private String fileExt;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "time")
    private int time;

    @ColumnInfo(name = "folder")
    private String folder;

    @ColumnInfo(name = "deleted")
    private int deleted;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getoPath() {
        return oPath;
    }

    public void setoPath(String oPath) {
        this.oPath = oPath;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
