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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 411;
    private ArrayList<MovieDetails> movieList;

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
        movieList = new ArrayList<>();
        MainScreenAdapter adapter = new MainScreenAdapter(movieList, listener);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        startLoader();
    }

    private void startLoader() {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("sortBy", 0);
        if (getSupportLoaderManager().getLoader(LOADER_ID) != null)
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        else
            getSupportLoaderManager().restartLoader(LOADER_ID, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Nitin", "loaderCreated");
        return new MovieLoader(this, args.getInt("sortBy"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray results = obj.getJSONArray("results");
            int length = results.length();
            for (int i = 0; i < length; i++) {
                JSONObject movie = results.getJSONObject(i);
                movieList.add(getMovie(movie));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Nitin", movieList.get(0).toString());

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    MovieDetails getMovie(JSONObject movie) throws JSONException {

        long id = movie.getLong("id");
        double voteAvg = movie.getDouble("vote_average");
        String name = movie.getString("title");
        String posterPath = movie.getString("poster_path");
        String overview = movie.getString("overview");
        String date = movie.getString("release_date");

        MovieDetails movieDetails = new MovieDetails(id, name, posterPath, date, overview, voteAvg);
        return movieDetails;
    }
}
