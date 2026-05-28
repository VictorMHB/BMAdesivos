package com.github.victormhb.bmadesivos.dto.ordem;

import com.github.victormhb.bmadesivos.dto.ordem.ItemOrdemDTO;

import java.util.List;

public record OrdemProducaoDTO(
        Long funcionarioId,
        Long clienteId,
        List<ItemOrdemDTO> itens
) {}