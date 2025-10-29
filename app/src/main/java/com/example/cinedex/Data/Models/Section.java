package com.example.cinedex.Data.Models;

import java.util.List;

public class Section {
    private String title;
    private List<Movie> movieList;

    public Section(String title, List<Movie> movieList) {
        this.title = title;
        this.movieList = movieList;
    }

    public String getTitle() {
        return title;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }
}