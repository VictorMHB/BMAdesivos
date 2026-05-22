package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.DashboardResponseDTO;
import com.github.victormhb.bmadesivos.dto.DashboardResponseDTO.*;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import com.github.victormhb.bmadesivos.repository.OrdemProducaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class DashboardService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final InsumoRepository insumoRepository;

    @Autowired
    public DashboardService(OrdemProducaoRepository ordemProducaoRepository,
                            InsumoRepository insumoRepository) {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.insumoRepository = insumoRepository;
    }

    public DashboardResponseDTO getDados() {
        return new DashboardResponseDTO(
                getResumoOrdens(),
                getResumoEstoque()
        );
    }

    private OrdemResumoDTO getResumoOrdens() {
        long pendentes = ordemProducaoRepository.countByStatus(StatusOrdem.PENDENTE);
        long emProducao = ordemProducaoRepository.countByStatus(StatusOrdem.EM_PRODUCAO);
        long totalAtivas = pendentes + emProducao;

        long concluidas = ordemProducaoRepository.countByStatusAndDataConclusaoAfter(
                StatusOrdem.CONCLUIDO,
                LocalDateTime.now().minusMonths(1)
        );

        List<OrdemMensalDTO> porMes = gerarConcluidasPorMes();

        return new OrdemResumoDTO(totalAtivas, pendentes, emProducao, concluidas, porMes);
    }

    private List<OrdemMensalDTO> gerarConcluidasPorMes() {
        return java.util.stream.IntStream.rangeClosed(1, 6)
                .mapToObj(i -> {
                    LocalDateTime inicio = LocalDateTime.now().minusMonths(6 - i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                    LocalDateTime fim = inicio.plusMonths(1);
                    long total = ordemProducaoRepository.countByStatusAndDataConclusaoBetween(
                            StatusOrdem.CONCLUIDO, inicio, fim);
                    String mes = inicio.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
                    return new OrdemMensalDTO(mes, total);
                })
                .toList();
    }

    private EstoqueResumoDTO getResumoEstoque() {
        List<Insumo> insumos = insumoRepository.findAll()
                .stream()
                .filter(Insumo::isAtivo)
                .toList();

        List<InsumoAlertaDTO> alertas = insumos.stream()
                .filter(i -> i.getEstoqueMinimo() != null)
                .filter(i -> i.getEstoqueAtual() <= i.getEstoqueMinimo())
                .map(i -> {
                    String nivel = i.getEstoqueAtual() <= 0 ? "CRITICO"
                            : i.getEstoqueAtual() <= i.getEstoqueMinimo() * 0.3 ? "CRITICO" : "ALERTA";
                    return new InsumoAlertaDTO(
                            i.getId(),
                            i.getNome(),
                            i.getTipoInsumo().name(),
                            i.getEstoqueAtual(),
                            i.getEstoqueMinimo(),
                            i.getUnidadeMedida(),
                            nivel
                    );
                })
                .sorted((a, b) -> a.nivel().compareTo(b.nivel()))
                .toList();

        long criticos = alertas.stream().filter(a -> a.nivel().equals("CRITICO")).count();
        long alertaCount = alertas.stream().filter(a -> a.nivel().equals("ALERTA")).count();
        long ok = insumos.size() - criticos - alertaCount;

        return new EstoqueResumoDTO(insumos.size(), criticos, alertaCount, ok, alertas);
    }
}