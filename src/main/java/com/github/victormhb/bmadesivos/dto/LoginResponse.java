package com.github.victormhb.bmadesivos.dto;

public class LoginResponse {
    private String token;
    private String nome;
    private String cargo;

    public LoginResponse(String token, String nome, String cargo) {
        this.token = token;
        this.nome = nome;
        this.cargo = cargo;
    }

    public String getToken() {
        return token;
    }

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }
}
