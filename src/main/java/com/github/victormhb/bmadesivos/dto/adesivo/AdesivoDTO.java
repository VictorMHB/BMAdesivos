package com.github.victormhb.bmadesivos.dto.adesivo;

import com.github.victormhb.bmadesivos.enums.TipoAdesivo;

public record AdesivoDTO(
        String nome,
        String descricao,
        TipoAdesivo tipoAdesivo,
        Double comprimento,
        Double altura,
        Double valorUnitario,
        Long clienteId,
        Long substratoId,
        Long resinaId
) {}