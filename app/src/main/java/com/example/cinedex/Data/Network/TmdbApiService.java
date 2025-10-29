package com.example.cinedex.Data.Network;

import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey
    );

    @GET("Usuario")
    Call<List<Usuario>> GetUsuario();

    @POST("Usuario")
    Call<Usuario> PostUsuario(@Body Usuario oUsuario);
}
