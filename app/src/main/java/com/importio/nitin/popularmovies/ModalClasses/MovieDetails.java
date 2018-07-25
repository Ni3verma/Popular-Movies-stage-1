package com.importio.nitin.popularmovies.ModalClasses;

public class MovieDetails {
    private long id;
    public String movieTitle;
    public String posterPath;
    public String releaseDate;
    public String synopsis;

    public long getId() {
        return id;
    }

    public double voteAverage;

    public MovieDetails(long id, String title, String imagePath, String date, String synp, double voteAvg) {
        this.movieTitle = title;
        this.posterPath = makeProperImagePath(imagePath);
        this.releaseDate = date;
        this.synopsis = synp;
        this.voteAverage = voteAvg;
        this.id = id;
    }

    private String makeProperImagePath(String s) {
        return "https://image.tmdb.org/t/p/w342" + s;
    }

    @Override
    public String toString() {
        return "\n\t[" +
                "\nid=" + id +
                "\nname=" + movieTitle +
                "\nvoteAvg=" + voteAverage +
                "\nPoster path=" + posterPath +
                "\n\t]";
    }
}
