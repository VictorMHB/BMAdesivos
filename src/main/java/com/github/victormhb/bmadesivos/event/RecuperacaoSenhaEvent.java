package com.github.victormhb.bmadesivos.event;

public class RecuperacaoSenhaEvent {
    private final String email;
    private final String nome;
    private final String token;

    public RecuperacaoSenhaEvent(String email, String nome, String token) {
        this.email = email;
        this.nome = nome;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getToken() {
        return token;
    }
}
