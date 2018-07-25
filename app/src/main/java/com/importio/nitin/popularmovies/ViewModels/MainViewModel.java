package com.importio.nitin.popularmovies.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.importio.nitin.popularmovies.Database.AppDatabase;
import com.importio.nitin.popularmovies.Database.FavouriteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<FavouriteEntry>> fav;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("Nitin", "main view model made");
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        fav = database.FavDao().getAllFavMovies();
    }

    public LiveData<List<FavouriteEntry>> getFavMovies() {
        return fav;
    }
}
