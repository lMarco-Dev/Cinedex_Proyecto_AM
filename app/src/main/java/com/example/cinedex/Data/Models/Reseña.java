package com.example.cinedex.Data.Models;

import com.example.cinedex.Data.Models.DTOs.UsuarioPublicoDto;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Reseña {
    @SerializedName("IdReseña")
    private int idReseña;

    @SerializedName("IdUsuario")
    private int idUsuario;

    @SerializedName("IdPelicula")
    private int idPelicula;

    @SerializedName("Comentario")
    private String comentario;

    @SerializedName("Calificacion")
    private float calificacion;

    @SerializedName("Fecha")
    private Date fecha;

    // --- Objetos Anidados (para GET) ---
    @SerializedName("Usuario")
    private UsuarioPublicoDto usuario;

    @SerializedName("Pelicula")
    private Movie pelicula; // Reutilizamos tu modelo Movie existente

    // Constructor para CREAR una reseña (POST)
    public Reseña(int idUsuario, int idPelicula, String comentario, float calificacion) {
        this.idUsuario = idUsuario;
        this.idPelicula = idPelicula;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    // (Getters)
    public int getIdReseña() { return idReseña; }
    public String getComentario() { return comentario; }
    public float getCalificacion() { return calificacion; }
    public Date getFecha() { return fecha; }
    public UsuarioPublicoDto getUsuario() { return usuario; }
    public Movie getPelicula() { return pelicula; }
}
