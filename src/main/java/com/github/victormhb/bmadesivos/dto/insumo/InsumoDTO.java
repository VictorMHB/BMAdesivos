package com.github.victormhb.bmadesivos.dto.insumo;

import com.github.victormhb.bmadesivos.enums.TamanhoEmbalagem;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;

public record InsumoDTO(
        String nome,
        String descricao,
        TipoInsumo tipoInsumo,
        Double estoqueAtual,
        Double valorUnitario,
        // Substrato
        Double largura,
        Double comprimento,
        Double metrosQuadrados,
        Integer quantidadeRolos,
        // Tinta
        String cor,
        TamanhoEmbalagem tamanhoEmbalagem
) {}