package com.importio.nitin.popularmovies.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.importio.nitin.popularmovies.Database.AppDatabase;
import com.importio.nitin.popularmovies.Database.FavouriteEntry;

public class GetFavStateViewModel extends ViewModel {
    private LiveData<FavouriteEntry> movie;

    public GetFavStateViewModel(AppDatabase database, long id) {
        movie = database.FavDao().getFavById(id);
    }

    public LiveData<FavouriteEntry> getMovie() {
        return movie;
    }
}
