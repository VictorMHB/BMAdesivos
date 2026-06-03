package com.github.victormhb.bmadesivos.dto.movimentacao;

import com.github.victormhb.bmadesivos.enums.TipoMovimentacao;

public record MovimentacaoDTO(
        Long insumoId,
        Long adesivoId,
        Double quantidade,
        TipoMovimentacao tipo,
        Double valorUnitario,
        String observacao
) { }
