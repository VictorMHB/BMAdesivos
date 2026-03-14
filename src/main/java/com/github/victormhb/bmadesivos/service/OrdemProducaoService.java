package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.*;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.InsumoRepository;
import com.github.victormhb.bmadesivos.repository.OrdemProducaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final AdesivoService adesivoService;
    private final FichaTecnicaService fichaTecnicaService;
    private final FuncionarioRepository funcionarioRepository;
    private final InsumoService insumoService;
    private final MovimentacaoService movimentacaoService;

    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                AdesivoService adesivoService,
                                InsumoRepository insumoRepository,
                                FichaTecnicaService fichaTecnicaService,
                                FuncionarioRepository funcionarioRepository,
                                InsumoService insumoService,
                                MovimentacaoService movimentacaoService)
    {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.adesivoService = adesivoService;
        this.fichaTecnicaService = fichaTecnicaService;
        this.funcionarioRepository = funcionarioRepository;
        this.insumoService = insumoService;
        this.movimentacaoService = movimentacaoService;
    }

    public List<OrdemProducao> listar() {
        return ordemProducaoRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public OrdemProducao abrirOrdem(OrdemProducaoDTO dto) throws Exception {
        Adesivo adesivo = adesivoService.buscarPorId(dto.produtoId());

        if (adesivo.getCliente() == null) {
            throw new Exception("O adesivo selecionado não possui um cliente vinculado.");
        }

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new Exception("Funcionário não encontrado."));

        OrdemProducao ordemProducao = new OrdemProducao();
        ordemProducao.setAdesivo(adesivo);
        ordemProducao.setCliente(adesivo.getCliente());
        ordemProducao.setFuncionario(funcionario);
        ordemProducao.setQtdPedida(dto.qtdPedida());
        ordemProducao.setStatus(StatusOrdem.PENDENTE);
        ordemProducao.setDataAbertura(LocalDateTime.now());
        ordemProducao.setAtivo(true);

        return ordemProducaoRepository.save(ordemProducao);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrdemProducao finalizarOrdem(Long id) throws Exception {
        OrdemProducao ordemProducao = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de Produção não encontrada."));

        if (ordemProducao.getStatus().equals(StatusOrdem.CONCLUIDO)) {
            throw new Exception("Esta ordem já foi finalizada.");
        }

        Adesivo adesivo = ordemProducao.getAdesivo();

        if (adesivo.getComprimento() == null || adesivo.getAltura() == null) {
            throw new Exception("O adesivo não possui dimensões cadastradas.");
        }

//        List<FichaTecnica> itensFicha = fichaTecnicaService
//                .buscarReceitaAdesivo(adesivo.getId());
//
//        if (itensFicha.isEmpty()) {
//            throw new Exception("Adesivo não possui uma Ficha Técnica cadastrada.");
//        }
//
//        // Calcula área do adesivo em m² (dimensões em cm)
//        Double areaAdesivo = (adesivo.getAltura() * adesivo.getComprimento()) / 10000;
//
//        for (FichaTecnica ficha : itensFicha) {
//            // quantidade da ficha = fator de consumo do insumo por m²
//            double consumoInsumo = ordemProducao.getQtdPedida() * areaAdesivo * ficha.getQuantidade();
//
//            insumoService.baixarEstoque(ficha.getInsumo().getId(), consumoInsumo);
//
//            MovimentacaoEstoque movInsumo = new MovimentacaoEstoque();
//            movInsumo.setInsumo(ficha.getInsumo());
//            movInsumo.setQuantidade(-consumoInsumo);
//            movInsumo.setValorUnitario(ficha.getInsumo().getValorUnitario());
//            movInsumo.setTipo(MovimentacaoEstoque.TipoMovimentacao.SAIDA_INSUMO);
//            movInsumo.setObservacao("Consumo automático para Ordem de Produção #" + ordemProducao.getId());
//
//            movimentacaoService.registar(movInsumo);
//        }

        MovimentacaoEstoque movProduto = new MovimentacaoEstoque();
        movProduto.setProduto(adesivo);
        movProduto.setQuantidade(ordemProducao.getQtdPedida().doubleValue());
        movProduto.setTipo(MovimentacaoEstoque.TipoMovimentacao.ENTRADA_PRODUTO);
        movProduto.setValorUnitario(adesivo.getValorUnitario());
        movProduto.setObservacao("Produção concluída para Ordem #" + ordemProducao.getId());

        movimentacaoService.registar(movProduto);

        ordemProducao.setStatus(StatusOrdem.CONCLUIDO);
        ordemProducao.setDataConclusao(LocalDateTime.now());

        return ordemProducaoRepository.save(ordemProducao);
    }

    @Transactional
    public void cancelarOrdem(Long id) throws Exception {
        OrdemProducao ordemProducao = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de Produção não encontrada."));

        if (ordemProducao.getStatus().equals(StatusOrdem.CONCLUIDO)) {
            throw new Exception("Não é possível cancelar uma ordem já concluída.");
        }

        ordemProducao.setStatus(StatusOrdem.CANCELADO);
        ordemProducaoRepository.save(ordemProducao);
    }
}