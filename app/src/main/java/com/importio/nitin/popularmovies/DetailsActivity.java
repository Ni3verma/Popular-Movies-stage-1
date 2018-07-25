package com.importio.nitin.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.importio.nitin.popularmovies.Database.AppDatabase;
import com.importio.nitin.popularmovies.Database.FavouriteEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "Nitin";

    private Review[] reviews;
    private Video[] videos;
    ListView reviewListView;
    ListView videoListView;
    private ReviewAdapter reviewAdapter;
    private VideoAdapter videoAdapter;
    MovieDetails selectedMovie;
    private AppDatabase mDatabase;

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

        initFavState();

        new getMovieReviewsTask().execute(selectedMovie.getId());
        new getMovieTrailerTask().execute(selectedMovie.getId());
    }

    public void initFavState() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavouriteEntry fav = mDatabase.FavDao().getFavById(selectedMovie.getId());
                if (fav != null) {
                    ImageButton button = findViewById(R.id.fav_button);
                    button.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
    }

    public void favClicked(View view) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavouriteEntry fav = mDatabase.FavDao().getFavById(selectedMovie.getId());
                if (fav == null) {
                    Log.d(TAG, "movie is not fav,so making it");
                    mDatabase.FavDao().insertFavMovie(new FavouriteEntry(selectedMovie.getId(), true));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageButton button = findViewById(R.id.fav_button);
                            button.setImageResource(R.drawable.ic_favorite);
                            Toast.makeText(DetailsActivity.this, "Added to favourite", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Log.d(TAG, "movie is fav,so deleting it");
                    mDatabase.FavDao().deleteFavById(selectedMovie.getId());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageButton button = findViewById(R.id.fav_button);
                            button.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(DetailsActivity.this, "Removed from favourite", Toast.LENGTH_SHORT).show();
                        }
                    });
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

            reviewAdapter = new ReviewAdapter(getApplicationContext(), reviews);
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

            videoAdapter = new VideoAdapter(getApplicationContext(), videos);
            videoListView.setAdapter(videoAdapter);
        }
    }
}
