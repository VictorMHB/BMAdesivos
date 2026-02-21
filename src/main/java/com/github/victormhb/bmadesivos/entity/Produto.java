package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "produtos")
@SQLDelete(sql = "UPDATE produtos SET ativo = false WHERE id = ?")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private Double precoVenda;

    @Column(nullable = false)
    private Integer estoqueAtual;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Produto() {}

    public Produto(String nome, String descricao, Double precoVenda) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
        this.ativo = true;
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Integer estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
