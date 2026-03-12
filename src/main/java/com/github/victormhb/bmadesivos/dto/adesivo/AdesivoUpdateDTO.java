package com.github.victormhb.bmadesivos.dto.adesivo;

import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.enums.TipoAdesivo;

public class AdesivoUpdateDTO {
    private String nome;
    private String descricao;
    private TipoAdesivo tipoAdesivo;
    private Double comprimento;
    private Double altura;
    private Double precoVenda;
    private Long clienteId;
    private Boolean ativo;

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

    public Double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(Double precoVenda) { this.precoVenda = precoVenda; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}