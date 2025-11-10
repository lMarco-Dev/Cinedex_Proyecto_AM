package com.example.cinedex.Data.Models;

import com.google.gson.annotations.SerializedName;

public class ReseñaRequest {

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("id_pelicula")
    private int idPelicula;

    @SerializedName("reseña_texto")
    private String texto;

    @SerializedName("puntuacion")
    private float puntaje;

    public ReseñaRequest(int idUsuario, int idPelicula, String texto, float puntaje) {
        this.idUsuario = idUsuario;
        this.idPelicula = idPelicula;
        this.texto = texto;
        this.puntaje = puntaje;
    }
}
