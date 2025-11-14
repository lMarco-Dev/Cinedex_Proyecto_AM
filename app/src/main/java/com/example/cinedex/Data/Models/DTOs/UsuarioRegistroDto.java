package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class UsuarioRegistroDto {

    @SerializedName("NombreUsuario")
    private String nombreUsuario;

    @SerializedName("Email")
    private String email;

    @SerializedName("Contrasena")
    private String contrasena;

    @SerializedName("Nombres")
    private String nombres;

    @SerializedName("Apellidos")
    private String apellidos;

    public UsuarioRegistroDto(String nombreUsuario, String email, String contrasena, String nombres, String apellidos) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }
}
