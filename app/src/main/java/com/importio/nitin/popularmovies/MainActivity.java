package com.importio.nitin.popularmovies;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.importio.nitin.popularmovies.Adapters.MainScreenAdapter;
import com.importio.nitin.popularmovies.Database.FavouriteEntry;
import com.importio.nitin.popularmovies.ModalClasses.MovieDetails;
import com.importio.nitin.popularmovies.ViewModels.MainViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final int LOADER_ID = 411;
    private static int SORT_BY_CODE = 0;
    static ArrayList<MovieDetails> movieList;
    private MainScreenAdapter adapter;

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
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        };
        movieList = new ArrayList<>();
        adapter = new MainScreenAdapter(movieList, listener);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        Bundle queryBundle = new Bundle();
        queryBundle.putInt("sortBy", SORT_BY_CODE);

        //check in case of rotation
        if (SORT_BY_CODE != 2)
            getSupportLoaderManager().initLoader(LOADER_ID, queryBundle, this);
        else
            displayFavMovies();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("Nitin", "loaderCreated");
        assert args != null;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private MovieDetails getMovie(JSONObject movie) throws JSONException {

        long id = movie.getLong("id");
        double voteAvg = movie.getDouble("vote_average");
        String name = movie.getString("title");
        String posterPath = movie.getString("poster_path");
        String overview = movie.getString("overview");
        String date = movie.getString("release_date");

        return new MovieDetails(id, name, posterPath, date, overview, voteAvg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort:
                showCustomDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Sort By");
        dialog.setContentView(R.layout.dialog_sort_type);
        dialog.show();
        RadioGroup radioGroup = dialog.findViewById(R.id.sort_radio_group);
        radioGroup.check(radioGroup.getChildAt(SORT_BY_CODE).getId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SORT_BY_CODE = group.indexOfChild(group.findViewById(checkedId));
                Bundle queryBundle = new Bundle();
                queryBundle.putInt("sortBy", SORT_BY_CODE);
                movieList.clear();
                if (SORT_BY_CODE == 2) {
                    displayFavMovies();
                } else
                    getSupportLoaderManager().restartLoader(LOADER_ID, queryBundle, MainActivity.this);
                dialog.dismiss();
            }
        });
    }

    void displayFavMovies() {
        //so that after coming back to main activity from details of a fav movie
        //popular movies are not shown by loader
        getSupportLoaderManager().destroyLoader(LOADER_ID);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavMovies().observe(this, new Observer<List<FavouriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteEntry> favouriteEntries) {
                movieList.clear();
                Log.d("Nitin", "updating from live data");
                for (FavouriteEntry fav : favouriteEntries) {
                    long id = fav.getMovieId();
                    new getFavMovieTask().execute(id);
                }
            }
        });
    }

    class getFavMovieTask extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... params) {
            return NetworkUtils.getMovieDetailsById(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(MainActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(s);
                movieList.add(getMovie(obj));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
