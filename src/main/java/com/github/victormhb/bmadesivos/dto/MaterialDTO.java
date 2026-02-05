package com.github.victormhb.bmadesivos.dto;

public record MaterialDTO(
        String nome,
        String unidadeMedida,
        Double estoqueAtual,
        Double estoqueMinimo,
        Double custoUnitario
)
{ }
