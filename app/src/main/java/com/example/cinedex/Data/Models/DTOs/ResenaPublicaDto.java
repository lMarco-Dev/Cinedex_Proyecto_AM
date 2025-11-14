package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class ResenaPublicaDto {

    @SerializedName("IdResena")   // Si cambiaste la ñ, usa esto
    private int idResena;

    @SerializedName("Comentario")
    private String comentario;

    @SerializedName("Puntuacion")
    private double puntuacion;

    @SerializedName("Fecha")
    private String fecha;

    // Usuario
    @SerializedName("IdUsuario")
    private int idUsuario;

    @SerializedName("NombreUsuario")
    private String nombreUsuario;

    // Película TMDB
    @SerializedName("IdPelicula")
    private int idPelicula;

    @SerializedName("TituloPelicula")
    private String tituloPelicula;

    @SerializedName("PosterPeliculaURL")
    private String posterPeliculaUrl;

    public int getIdResena() { return idResena; }
    public String getComentario() { return comentario; }
    public double getPuntuacion() { return puntuacion; }
    public String getFecha() { return fecha; }
    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public int getIdPelicula() { return idPelicula; }
    public String getTituloPelicula() { return tituloPelicula; }
    public String getPosterPeliculaUrl() { return posterPeliculaUrl; }
}
