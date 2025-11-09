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

    // Constructor
    public UsuarioRegistroDto(String nombreUsuario, String email, String contrasena, String nombres, String apellidos) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }
}
