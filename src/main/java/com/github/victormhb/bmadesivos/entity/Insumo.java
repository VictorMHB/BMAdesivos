package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "insumos")
@SQLDelete(sql = "UPDATE insumos SET ativo = false WHERE id = ?")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double estoqueAtual;

    @Column(nullable = false)
    private Double estoqueMinimo;

    @Column(nullable = false)
    private String unidadeMedida;

    private Double valorUnitario;

    @Column(nullable = false)
    private boolean ativo = true;

    public Insumo() {

    }

    public Insumo(String nome, Double estoqueAtual, Double estoqueMinimo, String unidadeMedida, Double valorUnitario) {
        this.nome = nome;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.unidadeMedida = unidadeMedida;
        this.valorUnitario = valorUnitario;
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

    public Double getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Double estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public Double getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Double estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
