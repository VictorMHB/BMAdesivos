package com.github.victormhb.bmadesivos.dto.movimentacao;

import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import java.time.LocalDateTime;

public record MovimentacaoResponseDTO(
        Long id,
        String insumoNome,
        String adesivoNome,
        Double quantidade,
        Double valorUnitario,
        Double valorTotal,
        String tipo,
        LocalDateTime dataHora,
        String observacao
) {
    public static MovimentacaoResponseDTO de(MovimentacaoEstoque m) {
        return new MovimentacaoResponseDTO(
                m.getId(),
                m.getInsumo() != null ? m.getInsumo().getNome() : null,
                m.getAdesivo() != null ? m.getAdesivo().getNome() : null,
                m.getQuantidade(),
                m.getValorUnitario(),
                m.getValorUnitario() != null ? m.getValorUnitario() * m.getQuantidade() : null,
                m.getTipo().name(),
                m.getDataHora(),
                m.getObservacao()
        );
    }
}