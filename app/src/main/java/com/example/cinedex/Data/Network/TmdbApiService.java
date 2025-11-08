package com.example.cinedex.Data.Network;

import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    // Agrega esta
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    // Agrega esta
    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    // 1. Para "En Cartelera"
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    // 2. Para "Tendencias de la Semana"
    @GET("trending/movie/week")
    Call<MovieResponse> getTrendingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("Usuario")
    Call<List<Usuario>> GetUsuario();

    @POST("Usuario")
    Call<Usuario> PostUsuario(@Body Usuario oUsuario);
}
