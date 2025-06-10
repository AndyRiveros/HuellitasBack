package com.example.tpLabo.dto;

public class ResetearContrasenaRequest {
    private String token;
    private String nuevaClave;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaClave() {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}
