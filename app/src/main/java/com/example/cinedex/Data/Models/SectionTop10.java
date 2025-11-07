package com.example.cinedex.Data.Models;
import java.util.List;

// Este modelo es para la sección Top 10
public class SectionTop10 {
    private String title;
    private String subtitle;
    private List<Movie> movies;

    public SectionTop10(String title, String subtitle, List<Movie> movies) {
        this.title = title;
        this.subtitle = subtitle;
        this.movies = movies;
    }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public List<Movie> getMovies() { return movies; }
}