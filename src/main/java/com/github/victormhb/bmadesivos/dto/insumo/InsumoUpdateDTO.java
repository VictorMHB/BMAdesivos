package com.github.victormhb.bmadesivos.dto.insumo;

import com.github.victormhb.bmadesivos.enums.TamanhoEmbalagem;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;

public class InsumoUpdateDTO {
    private String nome;
    private String descricao;
    private TipoInsumo tipoInsumo;
    private Double estoqueAtual;
    private Double valorUnitario;
    private Double largura;
    private Double comprimento;
    private Double metrosQuadrados;
    private String cor;
    private TamanhoEmbalagem tamanhoEmbalagem;
    private Boolean ativo;

    public InsumoUpdateDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public TipoInsumo getTipoInsumo() { return tipoInsumo; }
    public void setTipoInsumo(TipoInsumo tipoInsumo) { this.tipoInsumo = tipoInsumo; }

    public Double getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(Double estoqueAtual) { this.estoqueAtual = estoqueAtual; }

    public Double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(Double valorUnitario) { this.valorUnitario = valorUnitario; }

    public Double getLargura() { return largura; }
    public void setLargura(Double largura) { this.largura = largura; }

    public Double getComprimento() { return comprimento; }
    public void setComprimento(Double comprimento) { this.comprimento = comprimento; }

    public Double getMetrosQuadrados() { return metrosQuadrados; }
    public void setMetrosQuadrados(Double metrosQuadrados) { this.metrosQuadrados = metrosQuadrados; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public TamanhoEmbalagem getTamanhoEmbalagem() { return tamanhoEmbalagem; }
    public void setTamanhoEmbalagem(TamanhoEmbalagem tamanhoEmbalagem) { this.tamanhoEmbalagem = tamanhoEmbalagem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}