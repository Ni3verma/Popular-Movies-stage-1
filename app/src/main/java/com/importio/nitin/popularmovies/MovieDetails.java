package com.importio.nitin.popularmovies;

public class MovieDetails {
    String movieTitle;
    String posterPath;
    String releaseDate;
    String synopsis;
    float voteAverage;
    MovieDetails(String title,String imagePath,String date,String synp,float voteAvg){
        this.movieTitle=title;
        this.posterPath=imagePath;
        this.releaseDate=date;
        this.synopsis=synp;
        this.voteAverage=voteAvg;
    }

}
