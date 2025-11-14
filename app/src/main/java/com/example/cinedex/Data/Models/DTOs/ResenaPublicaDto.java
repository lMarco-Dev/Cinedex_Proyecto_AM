package com.example.cinedex.Data.Models.DTOs;

public class ResenaPublicaDto {
    private int idResena;
    private String comentario;
    private float calificacion;
    private String fecha;
    private int idUsuario;
    private String nombreUsuario;
    private int idPelicula;
    private String tituloPelicula;
    private String posterPeliculaURL;

    public int getIdResena() {
        return idResena;
    }

    public String getComentario() {
        return comentario;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public int getIdPelicula() {
        return idPelicula;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public String getPosterPeliculaURL() {
        return posterPeliculaURL;
    }
}
