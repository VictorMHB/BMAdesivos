package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.FichaTecnica;

public record FichaTecnicaDTO(
        Integer quantidade,
        String substrato,
        Double comprimento,
        Double altura,
        FichaTecnica.TipoAdesivo tipoAdesivo,
        Double qtdResina,
        Double valorUnitario,
        Long produtoId,
        Long insumoId
) {}
