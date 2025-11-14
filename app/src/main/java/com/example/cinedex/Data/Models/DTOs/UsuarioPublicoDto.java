// Archivo: Data/Models/DTOs/UsuarioPublicoDto.java
package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class UsuarioPublicoDto {

    // --- CORREGIDO: De PascalCase a camelCase ---
    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("nombreUsuario")
    private String nombreUsuario;

    @SerializedName("nombres")
    private String nombres;

    @SerializedName("apellidos")
    private String apellidos;

    @SerializedName("nombreRango")
    private String nombreRango;

    // Constructor vacío (necesario para Gson)
    public UsuarioPublicoDto() { }

    // --- Getters (Estos no cambian) ---
    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getNombreRango() { return nombreRango; }
}