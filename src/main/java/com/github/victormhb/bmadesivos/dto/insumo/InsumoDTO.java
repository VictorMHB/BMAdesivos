package com.github.victormhb.bmadesivos.dto.insumo;

public record InsumoDTO(
        String nome,
        String unidadeMedida,
        Double estoqueAtual,
        Double estoqueMinimo,
        Double valorUnitario
)
{ }
