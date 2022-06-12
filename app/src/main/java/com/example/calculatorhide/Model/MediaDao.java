package com.example.calculatorhide.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Insert
    public void addData(MediaItem mediaItem);

    @Query("select * from hidden_file")
    public List<MediaItem> getMedia();


    @Query("select * from hidden_file WHERE type= :type AND deleted=:deleted")
    public List<MediaItem> getImagesMedia(String type,int deleted);


//    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE uri LIKE :uri)")
//    public String isFavorite(String uri);

    @Query("SELECT * from hidden_file WHERE path = :path")
    public MediaItem getFilesByPath(String path);

    //    @Delete
//    public void delete(FavoriteList favoriteList);
    @Query("DELETE FROM hidden_file")
    void delete();

    @Query("DELETE FROM hidden_file WHERE path = :path")
    void deleteByPath(String path);

    @Query("UPDATE hidden_file SET deleted=:value WHERE path = :path")
    void addtoRecycle(int value,String path);

    @Query("select * from hidden_file WHERE deleted=:deleted")
    public List<MediaItem> getRecycleData(int deleted);
}
