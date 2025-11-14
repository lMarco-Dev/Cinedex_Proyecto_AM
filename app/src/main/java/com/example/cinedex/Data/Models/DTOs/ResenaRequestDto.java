package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class ResenaRequestDto {

    @SerializedName("IdUsuario")
    private int idUsuario;

    @SerializedName("IdPeliculaTMDB")
    private int idPeliculaTMDB;

    @SerializedName("TituloPelicula")
    private String tituloPelicula;

    @SerializedName("PosterUrl")
    private String posterUrl;

    @SerializedName("Texto")
    private String texto;

    @SerializedName("Puntaje")
    private double puntaje;

    public ResenaRequestDto(int idUsuario, int idPeliculaTMDB, String tituloPelicula, String posterUrl, String texto, double puntaje) {
        this.idUsuario = idUsuario;
        this.idPeliculaTMDB = idPeliculaTMDB;
        this.tituloPelicula = tituloPelicula;
        this.posterUrl = posterUrl;
        this.texto = texto;
        this.puntaje = puntaje;
    }
    public int getIdUsuario() { return idUsuario; }
    public int getIdPeliculaTMDB() { return idPeliculaTMDB; }
    public String getTituloPelicula() { return tituloPelicula; }
    public String getPosterUrl() { return posterUrl; }
    public String getTexto() { return texto; }
    public double getPuntaje() { return puntaje; }
}
