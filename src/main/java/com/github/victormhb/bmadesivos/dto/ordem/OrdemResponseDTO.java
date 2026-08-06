package com.github.victormhb.bmadesivos.dto.ordem;

import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;

import java.time.LocalDateTime;
import java.util.List;

public record OrdemResponseDTO(
        Long id,
        String clienteNome,
        String funcionarioNome,
        List<ItemOrdemResponseDTO> itens,
        Double valorTotal,
        StatusOrdem status,
        boolean arquivada,
        LocalDateTime dataAbertura,
        LocalDateTime dataPrazo,
        LocalDateTime dataConclusao
) {
    public record ItemOrdemResponseDTO(
            Long id,
            Long adesivoId,
            String adesivoNome,
            String tipoAdesivo,
            Integer quantidade,
            Double valorUnitario
    ) {}

    public static OrdemResponseDTO de(OrdemProducao o) {
        List<ItemOrdemResponseDTO> itens = o.getItens().stream().map(i -> new ItemOrdemResponseDTO(
                i.getId(),
                i.getAdesivo().getId(),
                i.getAdesivo().getNome(),
                i.getAdesivo().getTipoAdesivo().name(),
                i.getQuantidade(),
                i.getAdesivo().getValorUnitario()
        )).toList();

        double valorTotal = itens.stream()
                .mapToDouble(i -> (i.valorUnitario() != null ? i.valorUnitario() : 0.0) * i.quantidade())
                .sum();

        return new OrdemResponseDTO(
                o.getId(),
                o.getCliente() != null ? o.getCliente().getNome() : null,
                o.getFuncionario() != null ? o.getFuncionario().getNome() : null,
                itens,
                valorTotal,
                o.getStatus(),
                o.isArquivada(),
                o.getDataAbertura(),
                o.getDataPrazo(),
                o.getDataConclusao()
        );
    }
}