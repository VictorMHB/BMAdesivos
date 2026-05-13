package com.github.victormhb.bmadesivos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fichas_tecnicas")
public class FichaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adesivo_id", nullable = false)
    private Adesivo adesivo;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(nullable = false)
    private boolean ativo = true;

    public FichaTecnica() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adesivo getAdesivo() {
        return adesivo;
    }

    public void setAdesivo(Adesivo adesivo) {
        this.adesivo = adesivo;
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
