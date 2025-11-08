package com.example.cinedex.Data.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    //Base url
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    //Utilizamos singleton para evitar crear objetos innecesarios
    public static TmdbApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Recibimos el JSON
                    .build();
        }

        return retrofit.create(TmdbApiService.class);
    }
}
