package com.example.cinedex.Data.Network;

import java.util.List;

import com.example.cinedex.Data.Models.DTOs.MensajeRespuestaDto;
import com.example.cinedex.Data.Models.DTOs.ResenaPublicaDto;
import com.example.cinedex.Data.Models.DTOs.ResenaRequestDto;
import com.example.cinedex.Data.Models.DTOs.ResenaEditarDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioActualizarDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioLoginDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.example.cinedex.Data.Models.DTOs.UsuarioRegistroDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CineDexApiService {

    // --- Usuarios ---
    @GET("api/Usuarios")
    Call<List<UsuarioPublicoDto>> getUsuarios();

    @GET("api/Usuarios/{id}")
    Call<UsuarioPublicoDto> getUsuario(@Path("id") int idUsuario);

    @POST("api/Usuarios")
    Call<UsuarioPublicoDto> registrarUsuario(@Body UsuarioRegistroDto registroDto);

    @PUT("api/Usuarios/{id}")
    Call<Void> actualizarUsuario(
            @Path("id") int idUsuario,
            @Body UsuarioActualizarDto actualizarDto
    );

    @DELETE("api/Usuarios/{id}")
    Call<Void> deleteUsuario(@Path("id") int idUsuario);

    @POST("api/Usuarios/login")
    Call<UsuarioPublicoDto> loginUsuario(@Body UsuarioLoginDto loginDto);

    // --- Reseñas ---
    @GET("api/Resenas")
    Call<List<ResenaPublicaDto>> getResenas();

    @GET("api/Resenas/{id}")
    Call<ResenaPublicaDto> getResena(@Path("id") int idResena);

    @GET("api/Resenas/usuario/{idUsuario}")
    Call<List<ResenaPublicaDto>> getResenasPorUsuario(@Path("idUsuario") int idUsuario);

    @GET("api/Resenas/pelicula/{idPelicula}")
    Call<List<ResenaPublicaDto>> getResenasPorPelicula(@Path("idPelicula") int idPelicula);

    // El backend devuelve un mensaje (MensajeRespuestaDto) al crear la reseña
    @POST("api/Resenas")
    Call<MensajeRespuestaDto> postResena(@Body ResenaRequestDto request);

    @PUT("api/Resenas/{id}")
    Call<Void> putResena(
            @Path("id") int idResena,
            @Body ResenaEditarDto request
    );

    @DELETE("api/Resenas/{id}")
    Call<Void> deleteResena(@Path("id") int idResena);
}
