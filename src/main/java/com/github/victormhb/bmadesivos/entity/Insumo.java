package com.github.victormhb.bmadesivos.entity;

import com.github.victormhb.bmadesivos.enums.TamanhoEmbalagem;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import jakarta.persistence.*;

@Entity
@Table(name = "insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInsumo tipoInsumo;

    @Column(nullable = false)
    private Double estoqueAtual;

    private Double valorUnitario;

    @Column
    private Double estoqueMinimo;

    @Column
    private String unidadeMedida;

    //Substrato
    private Double largura;
    private Double comprimento;
    private Double metrosQuadrados;
    private Integer quantidadeRolos;
    private Double valorRolo;

    //Tinta
    private String cor;

    @Enumerated(EnumType.STRING)
    private TamanhoEmbalagem tamanhoEmbalagem;


    @Column(nullable = false)
    private boolean ativo = true;

    public Insumo() {}

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

    public TipoInsumo getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(TipoInsumo tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public Double getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Double estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
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

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public Double getMetrosQuadrados() {
        return metrosQuadrados;
    }

    public void setMetrosQuadrados(Double metrosQuadrados) {
        this.metrosQuadrados = metrosQuadrados;
    }

    public Integer getQuantidadeRolos() {
        return quantidadeRolos;
    }

    public void setQuantidadeRolos(Integer quantidadeRolos) {
        this.quantidadeRolos = quantidadeRolos;
    }

    public Double getValorRolo() {
        return valorRolo;
    }

    public void setValorRolo(Double valorRolo) {
        this.valorRolo = valorRolo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public TamanhoEmbalagem getTamanhoEmbalagem() {
        return tamanhoEmbalagem;
    }

    public void setTamanhoEmbalagem(TamanhoEmbalagem tamanhoEmbalagem) {
        this.tamanhoEmbalagem = tamanhoEmbalagem;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
