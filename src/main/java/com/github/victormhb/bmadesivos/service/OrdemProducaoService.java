package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.*;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import com.github.victormhb.bmadesivos.enums.TipoInsumo;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.OrdemProducaoRepository;
import com.github.victormhb.bmadesivos.repository.FichaTecnicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final AdesivoService adesivoService;
    private final FuncionarioRepository funcionarioRepository;
    private final InsumoService insumoService;
    private final FichaTecnicaRepository fichaTecnicaRepository;

    @Autowired
    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                AdesivoService adesivoService,
                                FuncionarioRepository funcionarioRepository,
                                InsumoService insumoService,
                                FichaTecnicaRepository fichaTecnicaRepository) {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.adesivoService = adesivoService;
        this.funcionarioRepository = funcionarioRepository;
        this.insumoService = insumoService;
        this.fichaTecnicaRepository = fichaTecnicaRepository;
    }

    public List<OrdemProducao> listar() {
        return ordemProducaoRepository.findByArquivadaFalseAndStatusNot(StatusOrdem.CANCELADO,
                Sort.by(Sort.Direction.DESC, "dataAbertura"));
    }

    public List<OrdemProducao> listarHistorico() {
        return ordemProducaoRepository.findByArquivadaTrueOrStatus(StatusOrdem.CANCELADO,
                Sort.by(Sort.Direction.DESC, "dataAbertura"));
    }

    public OrdemProducao buscarPorId(Long id) throws Exception {
        return ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de produção não encontrada."));
    }

    @Transactional
    public OrdemProducao abrirOrdem(OrdemProducaoDTO dto) throws Exception {
        if (dto.adesivoId() == null) throw new Exception("Adesivo é obrigatório.");
        if (dto.funcionarioId() == null) throw new Exception("Funcionário é obrigatório.");
        if (dto.qtdPedida() == null || dto.qtdPedida() <= 0)
            throw new Exception("Quantidade deve ser maior que zero.");

        Adesivo adesivo = adesivoService.buscarPorId(dto.adesivoId());

        if (adesivo.getCliente() == null)
            throw new Exception("O adesivo selecionado não possui cliente vinculado.");

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new Exception("Funcionário não encontrado."));

        OrdemProducao ordem = new OrdemProducao();
        ordem.setAdesivo(adesivo);
        ordem.setCliente(adesivo.getCliente());
        ordem.setFuncionario(funcionario);
        ordem.setQtdPedida(dto.qtdPedida());
        ordem.setStatus(StatusOrdem.PENDENTE);
        ordem.setDataAbertura(LocalDateTime.now());
        ordem.setAtivo(true);

        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao avancarStatus(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Ordem já concluída.");
        if (ordem.getStatus() == StatusOrdem.CANCELADO)
            throw new Exception("Ordem cancelada não pode ser avançada.");

        if (ordem.getStatus() == StatusOrdem.PENDENTE) {
            ordem.setStatus(StatusOrdem.EM_PRODUCAO);
        } else if (ordem.getStatus() == StatusOrdem.EM_PRODUCAO) {
            ordem.setStatus(StatusOrdem.CONCLUIDO);
            ordem.setDataConclusao(LocalDateTime.now());
        }

        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao finalizarOrdem(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Ordem já foi finalizada.");
        if (ordem.getStatus() == StatusOrdem.CANCELADO)
            throw new Exception("Ordem cancelada não pode ser finalizada.");

        List<FichaTecnica> itens = fichaTecnicaRepository
                .findByAdesivoAndAtivoTrue(ordem.getAdesivo());

        if (itens.isEmpty())
            throw new Exception("Adesivo não possui ficha técnica cadastrada.");

        for (FichaTecnica item : itens) {
            Insumo insumo = item.getInsumo();

            // Baixa substrato e resina proporcionalmente à quantidade pedida
            if (insumo.getTipoInsumo() == TipoInsumo.SUBSTRATO ||
                    insumo.getTipoInsumo() == TipoInsumo.RESINA) {

                if (item.getQuantidade() != null) {
                    double consumo = item.getQuantidade() * ordem.getQtdPedida();
                    insumoService.baixarEstoque(insumo.getId(), consumo);
                }
            }
            // Tinta — sem baixa automática
        }

        ordem.setStatus(StatusOrdem.CONCLUIDO);
        ordem.setDataConclusao(LocalDateTime.now());

        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao arquivarOrdem(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);
        if (ordem.getStatus() != StatusOrdem.CONCLUIDO) {
            throw new Exception("Apenas ordens concluídas podem ser arquivadas.");
        }
        ordem.setArquivada(true);
        return ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public OrdemProducao cancelarOrdem(Long id) throws Exception {
        OrdemProducao ordem = buscarPorId(id);

        if (ordem.getStatus() == StatusOrdem.CONCLUIDO)
            throw new Exception("Não é possível cancelar uma ordem já concluída.");

        ordem.setStatus(StatusOrdem.CANCELADO);
        return ordemProducaoRepository.save(ordem);
    }
}