package com.example.cinedex.Data.Network;

import com.example.cinedex.Data.Models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey
    );
}
