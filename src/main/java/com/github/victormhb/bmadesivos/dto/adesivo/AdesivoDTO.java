package com.github.victormhb.bmadesivos.dto.adesivo;

import com.github.victormhb.bmadesivos.entity.Adesivo;

public record AdesivoDTO(
        String nome,
        String descricao,
        Adesivo.TipoAdesivo tipoAdesivo,
        Double comprimento,
        Double altura,
        Double precoVenda,
        Long clienteId
) {}