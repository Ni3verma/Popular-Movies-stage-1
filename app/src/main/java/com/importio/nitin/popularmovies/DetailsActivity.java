package com.importio.nitin.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.importio.nitin.popularmovies.Adapters.ReviewAdapter;
import com.importio.nitin.popularmovies.Adapters.VideoAdapter;
import com.importio.nitin.popularmovies.Database.AppDatabase;
import com.importio.nitin.popularmovies.Database.FavouriteEntry;
import com.importio.nitin.popularmovies.ModalClasses.MovieDetails;
import com.importio.nitin.popularmovies.ModalClasses.Review;
import com.importio.nitin.popularmovies.ModalClasses.Video;
import com.importio.nitin.popularmovies.ViewModels.GetFavStateViewModel;
import com.importio.nitin.popularmovies.ViewModels.GetFavStateViewModelFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private Review[] reviews;
    private Video[] videos;
    ListView reviewListView;
    ListView videoListView;
    MovieDetails selectedMovie;
    private AppDatabase mDatabase;
    boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        selectedMovie = MainActivity.movieList.get(position);

        ImageView poster = findViewById(R.id.poster_movie);
        TextView name = findViewById(R.id.name_movie);
        TextView rating = findViewById(R.id.rating_movie);
        TextView date = findViewById(R.id.release_date_movie);
        TextView synopsis = findViewById(R.id.synopsis_movie);
        reviewListView = findViewById(R.id.review_lv);
        videoListView = findViewById(R.id.video_lv);
        mDatabase = AppDatabase.getsInstance(this);

        name.setText(selectedMovie.movieTitle);
        rating.setText(String.format(Locale.US, "%.1f", selectedMovie.voteAverage));
        date.setText(selectedMovie.releaseDate);
        synopsis.setText(selectedMovie.synopsis);
        Picasso.get().load(selectedMovie.posterPath).into(poster);

        reviewListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        videoListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        getFavState();

        new getMovieReviewsTask().execute(selectedMovie.getId());
        new getMovieTrailerTask().execute(selectedMovie.getId());
    }

    public void getFavState() {
        GetFavStateViewModelFactory factory = new GetFavStateViewModelFactory(mDatabase, selectedMovie.getId());
        GetFavStateViewModel viewModel = ViewModelProviders.of(this, factory).get(GetFavStateViewModel.class);
        viewModel.getMovie().observe(this, new Observer<FavouriteEntry>() {
            @Override
            public void onChanged(@Nullable FavouriteEntry favouriteEntry) {
                ImageButton button = findViewById(R.id.fav_button);
                if (favouriteEntry == null) {
                    isFav = false;
                    button.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    isFav = true;
                    button.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
    }

    public void favClicked(View view) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!isFav) {
                    isFav = true;
                    mDatabase.FavDao().insertFavMovie(new FavouriteEntry(selectedMovie.getId(), true));
                } else {
                    isFav = false;
                    mDatabase.FavDao().deleteFavById(selectedMovie.getId());
                }
            }
        });
    }

    class getMovieReviewsTask extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... param) {
            return NetworkUtils.getReviewsOfMovie(param[0]);
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (data == null) {
                Toast.makeText(DetailsActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(data);
                JSONArray results = obj.getJSONArray("results");
                int length = results.length();
                reviews = new Review[length];
                for (int i = 0; i < length; i++) {
                    JSONObject review = results.getJSONObject(i);
                    String author = review.getString("author");
                    String comment = review.getString("content");
                    Review c = new Review(author, comment);
                    reviews[i] = c;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ReviewAdapter reviewAdapter = new ReviewAdapter(getApplicationContext(), reviews);
            reviewListView.setAdapter(reviewAdapter);
        }
    }

    class getMovieTrailerTask extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... param) {
            return NetworkUtils.getVideosOfMovie(param[0]);
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if (data == null) {
                Toast.makeText(DetailsActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray results = obj.getJSONArray("results");
                int length = results.length();
                videos = new Video[length];
                for (int i = 0; i < length; i++) {
                    JSONObject video = results.getJSONObject(i);

                    videos[i] = new Video(video.getString("name"),
                            video.getString("site"),
                            video.getString("key"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), videos);
            videoListView.setAdapter(videoAdapter);
        }
    }
}
