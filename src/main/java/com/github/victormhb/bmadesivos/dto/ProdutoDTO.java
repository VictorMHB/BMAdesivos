package com.github.victormhb.bmadesivos.dto;

public record ProdutoDTO(
        String nome,
        String descricao,
        Double valorUnitario,
        Integer quantidade,
        Long clienteId
) { }
