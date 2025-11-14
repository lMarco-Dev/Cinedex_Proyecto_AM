package com.example.cinedex.Data.Models.DTOs;

import com.google.gson.annotations.SerializedName;

public class MensajeRespuestaDto {
    @SerializedName("mensaje")
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }
}
