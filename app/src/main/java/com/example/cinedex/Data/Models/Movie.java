package com.example.cinedex.Data.Models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    // Usa @SerializedName para mapear el nombre del JSON a tu variable
    @SerializedName("id")
    private int id; // -> Id
    @SerializedName("title")
    private String title; // -> Titulo

    @SerializedName("poster_path")
    private String posterPath; // -> Imagen

    @SerializedName("vote_count")
    private int voteCount; // -> Conteo de Votos

    @SerializedName("overview")
    private String overview; // -> Descripción

    @SerializedName("backdrop_path")
    private String backdropPath; // -> Imagen de fondo

    // --- ¡CAMPOS AÑADIDOS! ---
    @SerializedName("vote_average")
    private double voteAverage; // -> Calificación

    @SerializedName("release_date")
    private String releaseDate; // -> Fecha de estreno

    @SerializedName("runtime")
    private int runtime; // -> Duración en minutos

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public int getVoteCount() { return voteCount; }
    public String getOverview() { return overview; }
    public String getBackdropPath() { return backdropPath; }
    public double getVoteAverage() { return voteAverage; }
    public String getReleaseDate() { return releaseDate; }
    public int getRuntime() { return runtime; }
}