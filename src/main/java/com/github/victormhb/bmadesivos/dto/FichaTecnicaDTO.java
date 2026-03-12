package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.FichaTecnica;

public record FichaTecnicaDTO(
        Integer quantidade,
        Long adesivoId,
        Long insumoId
) {}
