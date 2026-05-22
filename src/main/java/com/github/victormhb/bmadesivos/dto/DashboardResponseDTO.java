package com.github.victormhb.bmadesivos.dto;

import java.util.List;

public record DashboardResponseDTO(
        OrdemResumoDTO ordens,
        EstoqueResumoDTO estoque
) {
    public record OrdemResumoDTO(
            long totalAtivas,
            long pendentes,
            long emProducao,
            long concluidas,
            List<OrdemMensalDTO> concluidasPorMes
    ) {}

    public record OrdemMensalDTO(
            String mes,
            long total
    ) {}

    public record EstoqueResumoDTO(
            long totalInsumos,
            long criticos,
            long alertas,
            long ok,
            List<InsumoAlertaDTO> insumosCriticos
    ) {}

    public record InsumoAlertaDTO(
            Long id,
            String nome,
            String tipoInsumo,
            Double estoqueAtual,
            Double estoqueMinimo,
            String unidadeMedida,
            String nivel
    ) {}
}