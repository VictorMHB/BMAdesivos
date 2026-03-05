package com.github.victormhb.bmadesivos.dto.auth;

public class LoginResponse {
    private Long id;
    private String token;
    private String nome;
    private String cargo;
    private boolean requerTrocarSenha;

    public LoginResponse(Long id, String token, String nome, String cargo, boolean requerTrocarSenha) {
        this.id = id;
        this.token = token;
        this.nome = nome;
        this.cargo = cargo;
        this.requerTrocarSenha = requerTrocarSenha;
    }

    public Long getId() {
        return id;
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

    public boolean isRequerTrocarSenha() {
        return requerTrocarSenha;
    }
}
