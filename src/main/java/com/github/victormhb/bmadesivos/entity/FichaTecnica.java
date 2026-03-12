package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "fichas_tecnicas")
@SQLDelete(sql = "UPDATE fichas_tecnicas SET ativo = false WHERE id = ?")
public class FichaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String substrato;

    @Column(nullable = false)
    private Double comprimento;

    @Column(nullable = false)
    private Double altura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAdesivo tipoAdesivo;

    @Column
    private Double qtdResina;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Adesivo produto;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(nullable = false)
    private boolean ativo = true;

    public enum TipoAdesivo {
        PLACA_ALUMINIO,
        ADESIVO,
        RESINADO
    }

    public FichaTecnica() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getSubstrato() {
        return substrato;
    }

    public void setSubstrato(String substrato) {
        this.substrato = substrato;
    }

    public Double getComprimento() {
        return comprimento;
    }

    public void setComprimento(Double comprimento) {
        this.comprimento = comprimento;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public TipoAdesivo getTipoAdesivo() {
        return tipoAdesivo;
    }

    public void setTipoAdesivo(TipoAdesivo tipoAdesivo) {
        this.tipoAdesivo = tipoAdesivo;
    }

    public Double getQtdResina() {
        return qtdResina;
    }

    public void setQtdResina(Double qtdResina) {
        this.qtdResina = qtdResina;
    }

    public Adesivo getProduto() {
        return produto;
    }

    public void setProduto(Adesivo produto) {
        this.produto = produto;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
