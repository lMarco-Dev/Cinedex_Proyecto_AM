package com.example.cinedex.Data.Models;

import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Resena {
    @SerializedName("id_reseña")
    private int idReseña;

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("id_pelicula")
    private int idPelicula;

    @SerializedName("reseña_texto")
    private String reseñaTexto;

    @SerializedName("puntuacion")
    private float puntuacion;

    @SerializedName("fecha_coleccion")
    private Date fechaColeccion;

    // --- Objetos Anidados (para GET) ---
    @SerializedName("Usuario")
    private UsuarioPublicoDto usuario;

    @SerializedName("Pelicula")
    private Movie pelicula;

    // Constructor vacio
    public Resena() {}

    // (Getters)
    public int getIdReseña() { return idReseña; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdPelicula() { return idPelicula; }
    public String getReseñaTexto() { return reseñaTexto; }
    public float getPuntuacion() { return puntuacion; }
    public Date getFechaColeccion() { return fechaColeccion; }
    public UsuarioPublicoDto getUsuario() { return usuario; }
    public Movie getPelicula() { return pelicula; }
    // dentro de la clase Reseña
    public void setIdReseña(int idReseña) { this.idReseña = idReseña; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setIdPelicula(int idPelicula) { this.idPelicula = idPelicula; }
    public void setReseñaTexto(String reseñaTexto) { this.reseñaTexto = reseñaTexto; }
    public void setPuntuacion(float puntuacion) { this.puntuacion = puntuacion; }

}
