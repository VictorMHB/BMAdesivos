package com.github.victormhb.bmadesivos.dto;

public record OrdemProducaoDTO(
    Long produtoId,
    Long funcionarioId,
    Integer qtdPedida
) { }
