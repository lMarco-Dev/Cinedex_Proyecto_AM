package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class ResenaEditarDto {

    @SerializedName("Texto")
    private String texto;

    @SerializedName("Puntaje")
    private double puntaje;

    public ResenaEditarDto(String texto, double puntaje) {
        this.texto = texto;
        this.puntaje = puntaje;
    }
}
