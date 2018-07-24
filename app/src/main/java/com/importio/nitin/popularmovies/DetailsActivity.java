package com.importio.nitin.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {
    private Review[] reviews;
    private String[] videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        MovieDetails selectedMovie = MainActivity.movieList.get(position);

        ImageView poster = findViewById(R.id.poster_movie);
        TextView name = findViewById(R.id.name_movie);
        TextView rating = findViewById(R.id.rating_movie);
        TextView date = findViewById(R.id.release_date_movie);
        TextView synopsis = findViewById(R.id.synopsis_movie);

        name.setText(selectedMovie.movieTitle);
        rating.setText(String.format(Locale.US, "%.1f", selectedMovie.voteAverage));
        date.setText(selectedMovie.releaseDate);
        synopsis.setText(selectedMovie.synopsis);
        Picasso.get().load(selectedMovie.posterPath).into(poster);

        new getMovieReviewsTask().execute(selectedMovie.getId());
        new getMovieTrailerTask().execute(selectedMovie.getId());
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
        }
    }

    class getMovieTrailerTask extends AsyncTask<Long, Void, String> {

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
                videos = new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject video = results.getJSONObject(i);
                    videos[i] = video.getString("key");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
