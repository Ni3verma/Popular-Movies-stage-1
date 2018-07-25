package com.importio.nitin.popularmovies.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("select * from favourite")
    List<FavouriteEntry> getAllFavMovies();

    @Insert
    void insertFavMovie(FavouriteEntry movie);

    @Delete
    void removeFavMovie(FavouriteEntry movie);

    @Query("delete from favourite where movieId=:id")
    void deleteFavById(long id);

    @Query("select * from favourite where movieId=:id")
    FavouriteEntry getFavById(long id);
}
