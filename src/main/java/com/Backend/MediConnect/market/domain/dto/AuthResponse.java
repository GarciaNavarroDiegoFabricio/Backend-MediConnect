package com.Backend.MediConnect.market.domain.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String rol;
    private String nombre;

    public AuthResponse(String token, String rol, String nombre) {
        this.token = token;
        this.rol = rol;
        this.nombre = nombre;
    }

}