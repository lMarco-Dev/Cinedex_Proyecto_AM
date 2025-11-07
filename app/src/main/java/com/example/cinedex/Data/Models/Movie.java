package com.example.cinedex.Data.Models;

import com.google.gson.annotations.SerializedName;

public class Movie {
    // Usa @SerializedName para mapear el nombre del JSON a tu variable
    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_count")
    private int voteCount;

    // Getters
    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public int getVoteCount() { return voteCount; }
}