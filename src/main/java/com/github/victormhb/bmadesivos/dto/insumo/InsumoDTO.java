package com.github.victormhb.bmadesivos.dto.insumo;

import com.github.victormhb.bmadesivos.enums.TipoInsumo;

public record InsumoDTO(
        String nome,
        String unidadeMedida,
        Double estoqueAtual,
        Double estoqueMinimo,
        Double valorUnitario,
        TipoInsumo tipoInsumo
)
{ }
