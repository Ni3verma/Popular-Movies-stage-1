package com.importio.nitin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class MovieLoader extends AsyncTaskLoader<String> {
    private int mSortByCode;
    private String result;  //cache result into it

    MovieLoader(@NonNull Context context, int sortByCode) {
        super(context);
        mSortByCode = sortByCode;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (result != null) {
            deliverResult(result);
        } else
            forceLoad(); //start the loadInBackground()method once the Loader is created.
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getMovies(mSortByCode);
    }

    @Override
    public void deliverResult(@Nullable String data) {
        result = data;
        super.deliverResult(data);
    }
}
