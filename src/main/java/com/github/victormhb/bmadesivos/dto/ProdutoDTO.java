package com.github.victormhb.bmadesivos.dto;

public record ProdutoDTO(
        String nome,
        String descricao,
        Double precoVenda,
        Integer estoqueAtual
) { }
