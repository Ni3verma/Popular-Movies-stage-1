package com.importio.nitin.popularmovies.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourite")
public class FavouriteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private long movieId;
    private boolean isFav;

    @Ignore
    public FavouriteEntry(long movieId, boolean isFav) {
        this.movieId = movieId;
        this.isFav = isFav;
    }

    public FavouriteEntry(int id, long movieId, boolean isFav) {
        this.id = id;
        this.movieId = movieId;
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
