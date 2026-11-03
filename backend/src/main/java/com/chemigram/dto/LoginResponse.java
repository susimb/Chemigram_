package com.chemigram.dto;

public class LoginResponse {

    private String token;
    private String tipo;
    private UsuarioResponse usuario;

    public LoginResponse(String token, UsuarioResponse usuario) {
        this.token = token;
        this.tipo = "Bearer";
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }
}