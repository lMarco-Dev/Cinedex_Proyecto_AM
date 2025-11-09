package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class UsuarioPublicoDto {

    @SerializedName("IdUsuario")
    private int idUsuario;

    @SerializedName("NombreUsuario")
    private String nombreUsuario;

    @SerializedName("Nombres")
    private String nombres;

    @SerializedName("Apellidos")
    private String apellidos;

    @SerializedName("NombreRango")
    private String nombreRango;

    // Constructor vacío (necesario para Gson)
    public UsuarioPublicoDto() { }

    // --- Getters ---
    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getNombreRango() { return nombreRango; }
}
