package com.github.victormhb.bmadesivos.dto.auth.login;

public class LoginResponse {
    private Long id;
    private String token;
    private String nome;
    private String cargo;
    private boolean requerTrocarSenha;
    private String email;
    private String telefone;

    public LoginResponse(Long id, String token, String nome, String cargo, boolean requerTrocarSenha, String email, String telefone) {
        this.id = id;
        this.token = token;
        this.nome = nome;
        this.cargo = cargo;
        this.requerTrocarSenha = requerTrocarSenha;
        this.email = email;
        this.telefone = telefone;
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

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}
