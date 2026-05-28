package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_ordem")
public class ItemOrdem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_id", nullable = false)
    private OrdemProducao ordem;

    @ManyToOne
    @JoinColumn(name = "adesivo_id", nullable = false)
    private Adesivo adesivo;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemOrdem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public OrdemProducao getOrdem() { return ordem; }
    public void setOrdem(OrdemProducao ordem) { this.ordem = ordem; }
    public Adesivo getAdesivo() { return adesivo; }
    public void setAdesivo(Adesivo adesivo) { this.adesivo = adesivo; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}