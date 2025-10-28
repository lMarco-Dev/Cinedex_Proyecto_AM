package com.example.cinedex.Models; // Usando 'models' en minúscula, como es estándar

import com.example.cinedex.Models.Movie;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {

    @SerializedName("results") // Usé "results" ya que es el nombre correcto del array en la API de TMDB
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}