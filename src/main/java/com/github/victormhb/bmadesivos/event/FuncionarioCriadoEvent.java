package com.github.victormhb.bmadesivos.event;

public class FuncionarioCriadoEvent {
    private final String email;
    private final String nome;
    private final String senhaTemp;

    public FuncionarioCriadoEvent(String email, String nome, String senhaTemp) {
        this.email = email;
        this.nome = nome;
        this.senhaTemp = senhaTemp;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSenhaTemp() {
        return senhaTemp;
    }
}
