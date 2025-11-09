package com.example.cinedex.Data.Network;

import java.util.List;

import com.example.cinedex.Data.Models.DTOs.UsuarioActualizarDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioRegistroDto;
import com.example.cinedex.Data.Models.Reseña;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CineDexApiService {

    // --- Endpoints de Usuarios (api/Usuarios) ---
    @GET("api/Usuarios")
    Call<List<UsuarioPublicoDto>> getUsuarios();

    @GET("api/Usuarios/{id}")
    Call<UsuarioPublicoDto> getUsuario(@Path("id") int idUsuario);

    @POST("api/Usuarios")
    Call<UsuarioPublicoDto> registrarUsuario(@Body UsuarioRegistroDto registroDto);

    @PUT("api/Usuarios/{id}")
    Call<Void> actualizarUsuario(@Path("id") int idUsuario, @Body UsuarioActualizarDto actualizarDto);

    @DELETE("api/Usuarios/{id}")
    Call<Void> deleteUsuario(@Path("id") int idUsuario);

    // --- Endpoints de Usuarios (api/Reseñas) ---
    @GET("api/Reseñas")
    Call<List<Reseña>> getResenas();

    @GET("api/Reseñas/{id}")
    Call<Reseña> getResena(@Path("id") int idResena);

    @GET("api/Reseñas/PorUsuario/{idUsuario}")
    Call<List<Reseña>> getResenasPorUsuario(@Path("idUsuario") int idUsuario);

    @GET("api/Reseñas/PorPelicula/{idPelicula}")
    Call<List<Reseña>> getResenasPorPelicula(@Path("idPelicula") int idPelicula);

    @POST("api/Reseñas")
    Call<Reseña> postResena(@Body Reseña resena);

    @PUT("api/Reseñas/{id}")
    Call<Void> putResena(@Path("id") int idResena, @Body Reseña resena);

    @DELETE("api/Reseñas/{id}")
    Call<Void> deleteResena(@Path("id") int idResena);

}

