package com.example.cinedex.Data.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CineDexApiClient {

    private static Retrofit retrofit = null;
    //URL somee
    private static final String BASE_URL = "http://cinedex.somee.com/";
    public static CineDexApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        // Devuelve la NUEVA INTERFAZ de CineDex
        return retrofit.create(CineDexApiService.class);
    }
}
