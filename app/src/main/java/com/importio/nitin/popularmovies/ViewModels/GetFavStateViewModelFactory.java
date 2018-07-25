package com.importio.nitin.popularmovies.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.importio.nitin.popularmovies.Database.AppDatabase;

public class GetFavStateViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final long id;
    private AppDatabase mDb;

    public GetFavStateViewModelFactory(AppDatabase mDb, long id) {
        this.mDb = mDb;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GetFavStateViewModel(mDb, id);
    }
}
