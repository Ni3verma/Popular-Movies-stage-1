package com.importio.nitin.popularmovies;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 411;
    private ArrayList<MovieDetails> movieList;
    MainScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String key = getResources().getString(R.string.API_KEY);
            NetworkUtils.setApiKey(key);
        } catch (Resources.NotFoundException ex) {
            throw new RuntimeException("Please provide API key");
        }

        RecyclerView recyclerView = findViewById(R.id.movieList_rv);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("Nitin", position + " clicked ");
            }
        };
        movieList = new ArrayList<>();
        adapter = new MainScreenAdapter(movieList, listener);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        Bundle queryBundle = new Bundle();
        queryBundle.putInt("sortBy", 0);

        getSupportLoaderManager().initLoader(LOADER_ID, queryBundle, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Nitin", "loaderCreated");
        return new MovieLoader(this, args.getInt("sortBy"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data == null) {  //no data was received
            Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject obj = new JSONObject(data);
            JSONArray results = obj.getJSONArray("results");
            int length = results.length();
            for (int i = 0; i < length; i++) {
                JSONObject movie = results.getJSONObject(i);
                movieList.add(getMovie(movie));

            }
            Log.d("Nitin", "result:\n" + movieList.get(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

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
