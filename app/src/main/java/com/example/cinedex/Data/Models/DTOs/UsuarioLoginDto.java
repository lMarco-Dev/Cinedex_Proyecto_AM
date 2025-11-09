package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class UsuarioLoginDto {
    @SerializedName("NombreUsuario")
    private String nombreUsuario;

    @SerializedName("Contrasena")
    private String contrasena;

    public UsuarioLoginDto(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // Getters (puedes omitirlos si prefieres, pero es buena práctica)
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}