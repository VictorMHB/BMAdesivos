package com.github.victormhb.bmadesivos.dto.adesivo;

import com.github.victormhb.bmadesivos.enums.TipoAdesivo;
import java.util.List;

public class AdesivoUpdateDTO {
    private String nome;
    private String descricao;
    private TipoAdesivo tipoAdesivo;
    private Double comprimento;
    private Double altura;
    private Double valorUnitario;
    private Long clienteId;
    private Boolean ativo;
    private Long substratoId;
    private List<Long> tintaIds;
    private Long resinaId;

    public AdesivoUpdateDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TipoAdesivo getTipoAdesivo() { return tipoAdesivo; }
    public void setTipoAdesivo(TipoAdesivo tipoAdesivo) { this.tipoAdesivo = tipoAdesivo; }

    public Double getComprimento() { return comprimento; }
    public void setComprimento(Double comprimento) { this.comprimento = comprimento; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(Double valorUnitario) { this.valorUnitario = valorUnitario; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Long getSubstratoId() { return substratoId; }
    public void setSubstratoId(Long substratoId) { this.substratoId = substratoId; }

    public List<Long> getTintaIds() { return tintaIds; }
    public void setTintaIds(List<Long> tintaIds) { this.tintaIds = tintaIds; }

    public Long getResinaId() { return resinaId; }
    public void setResinaId(Long resinaId) { this.resinaId = resinaId; }
}