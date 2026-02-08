package com.github.victormhb.bmadesivos.dto.cliente;

public class ClienteUpdateDTO {

    private String nome;
    private String cpfCnpj;
    private String email;
    private String telefone;
    private EnderecoDTO endereco;


    public ClienteUpdateDTO() {
    }

    // GETTERS E SETTERS --------------------

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public EnderecoDTO getEndereco() {return endereco;}

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(EnderecoDTO endereco) {this.endereco = endereco;}
}