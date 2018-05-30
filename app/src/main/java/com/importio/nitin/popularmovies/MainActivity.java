package com.importio.nitin.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 411;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = getResources().getString(R.string.API_KEY);
        NetworkUtils.setApiKey(key);

        RecyclerView recyclerView = findViewById(R.id.movieList_rv);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("Nitin", position + " clicked ");
            }
        };
        MainScreenAdapter adapter = new MainScreenAdapter(new ArrayList<MovieDetails>(), listener);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        startLoader();
    }

    private void startLoader() {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("sortBy", 0);
        if (getSupportLoaderManager().getLoader(LOADER_ID) != null)
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(LOADER_ID, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieLoader(this, args.getInt("sortBy"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        //parseJSON here
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
