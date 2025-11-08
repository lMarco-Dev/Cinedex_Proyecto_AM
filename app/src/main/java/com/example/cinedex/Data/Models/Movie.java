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
    private int voteCount; // -> Calificación

    @SerializedName("overview")
    private String overview; // -> Descripción

    @SerializedName("backdrop_path")
    private String backdropPath; // -> Imagen de fondo

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public int getVoteCount() { return voteCount; }
    public String getOverview() { return overview; }
    public String getBackdropPath() { return backdropPath; }
}