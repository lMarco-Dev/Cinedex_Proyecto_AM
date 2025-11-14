// Archivo: Data/Models/ReseñaRequest.java
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

    // --- CAMPOS AÑADIDOS ---
    @SerializedName("titulo_pelicula")
    private String tituloPelicula;

    @SerializedName("poster_url")
    private String posterUrl;
    // --- FIN DE CAMPOS AÑADIDOS ---


    // --- CONSTRUCTOR ACTUALIZADO (de 6 argumentos) ---
    public ReseñaRequest(int idUsuario, int idPelicula, String texto, float puntaje,
                         String tituloPelicula, String posterUrl) {
        this.idUsuario = idUsuario;
        this.idPelicula = idPelicula;
        this.texto = texto;
        this.puntaje = puntaje;
        this.tituloPelicula = tituloPelicula;
        this.posterUrl = posterUrl;
    }
}