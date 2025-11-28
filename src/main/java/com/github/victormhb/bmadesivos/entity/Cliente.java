package com.github.victormhb.bmadesivos.entity;

import br.com.caelum.stella.bean.validation.CNPJ;
import br.com.caelum.stella.bean.validation.CPF;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@SQLDelete(sql = "UPDATE clientes SET ativo = false WHERE id = ?")
//@SQLRestriction("ativo = true")
public class Cliente {

    @Id
    @GeneratedValue
    private Long  id;
    private String nome;

    @CPF(message = "CPF inválido")
    @CNPJ(message = "CNPJ inválido")
    private String cpfCnpj;

    private String email;
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @Column(name = "ativo")
    private boolean ativo = true;

    public Cliente() {}

    public Cliente(String nome, String cpfCnpj, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.telefone = telefone;
    }

    // GETTERS E SETTERS --------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
