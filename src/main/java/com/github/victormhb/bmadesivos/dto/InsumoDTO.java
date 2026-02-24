package com.github.victormhb.bmadesivos.dto;

public record InsumoDTO(
        String nome,
        String unidadeMedida,
        Double estoqueAtual,
        Double estoqueMinimo,
        Double valorUnitario
)
{ }
