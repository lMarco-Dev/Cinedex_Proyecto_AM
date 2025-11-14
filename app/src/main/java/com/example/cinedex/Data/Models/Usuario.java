package com.example.cinedex.Data.Models;

public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String contrasena;
    private String nombres;
    private String apellidos;
    private int idRangoActual;

    public Usuario() { }

    public Usuario(String nombreUsuario, String email, String contrasena, String nombres, String apellidos) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getIdRangoActual() {
        return idRangoActual;
    }

    public void setIdRangoActual(int idRangoActual) {
        this.idRangoActual = idRangoActual;
    }
}
