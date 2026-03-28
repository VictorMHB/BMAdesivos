package com.github.victormhb.bmadesivos.dto.adesivo;

import com.github.victormhb.bmadesivos.enums.TipoAdesivo;
import java.util.List;

public record AdesivoDTO(
        String nome,
        String descricao,
        TipoAdesivo tipoAdesivo,
        Double comprimento,
        Double altura,
        Double valorUnitario,
        Long clienteId,
        Long substratoId,
        List<Long> tintaIds,
        Long resinaId
) {}