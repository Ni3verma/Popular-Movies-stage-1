package com.importio.nitin.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    class getMovieTrailerTask extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... param) {
            return NetworkUtils.getReviewsOfMovie(param[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
