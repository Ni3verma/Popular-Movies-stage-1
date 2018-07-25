package com.importio.nitin.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private static String API_KEY = null;
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String API_PARAM = "api_key";

    public static String getMovies(int sortByCode) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(getSortBy(sortByCode))
                .appendQueryParameter(API_PARAM, API_KEY);

        URL requestURL = null;
        try {
            requestURL = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("Nitin", "Request URL = " + requestURL.toString());

        return getJSONResponse(requestURL);

    }

    private static String getJSONResponse(URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                //stream is empty, no point in parsing
                return null;
            }
            response = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;

    }

    public static void setApiKey(String key) {
        API_KEY = key;
    }

    private static String getSortBy(int code) {
        String sort = null;
        switch (code) {
            case 0:
                sort = "popular";
                break;
            case 1:
                sort = "top_rated";
                break;
        }
        return sort;
    }

    public static String getVideosOfMovie(long id) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + id)
                .appendPath("videos")
                .appendQueryParameter(API_PARAM, API_KEY);

        URL requestURL = null;
        try {
            requestURL = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("Nitin", "videos Request URL = " + requestURL.toString());

        return getJSONResponse(requestURL);
    }

    public static String getReviewsOfMovie(long id) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + id)
                .appendPath("reviews")
                .appendQueryParameter(API_PARAM, API_KEY);

        URL requestURL = null;
        try {
            requestURL = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("Nitin", "reviews Request URL = " + requestURL.toString());

        return getJSONResponse(requestURL);
    }

    public static String getMovieDetailsById(long id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + id)
                .appendQueryParameter(API_PARAM, API_KEY);

        URL requestURL = null;
        try {
            requestURL = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("Nitin", "getMovieDetailsById Request URL = " + requestURL.toString());

        return getJSONResponse(requestURL);
    }

}
