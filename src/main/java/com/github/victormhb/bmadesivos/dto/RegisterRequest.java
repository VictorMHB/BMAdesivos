package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.Funcionario;

public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
    private Funcionario.Cargo cargo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Funcionario.Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Funcionario.Cargo cargo) {
        this.cargo = cargo;
    }
}
