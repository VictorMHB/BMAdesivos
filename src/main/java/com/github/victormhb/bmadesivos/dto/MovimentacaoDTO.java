package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;

public record MovimentacaoDTO(
        Long materialId,
        Long produtoId,
        Double quantidade,
        MovimentacaoEstoque.TipoMovimentacao tipo,
        Double valorUnitario,
        String observacao
) { }
