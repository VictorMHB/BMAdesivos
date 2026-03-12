package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.*;
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
    private final AdesivoService produtoService;
    private final FichaTecnicaService fichaTecnicaService;
    private final FuncionarioRepository funcionarioRepository;
    private final InsumoService insumoService;
    private final MovimentacaoService movimentacaoService;

    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                AdesivoService produtoService,
                                InsumoRepository insumoRepository,
                                FichaTecnicaService fichaTecnicaService,
                                FuncionarioRepository funcionarioRepository,
                                InsumoService insumoService,
                                MovimentacaoService movimentacaoService)
    {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.produtoService = produtoService;
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
        Adesivo produto = produtoService.buscarPorId(dto.produtoId());

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new Exception("Funcionário não encontrado."));

        OrdemProducao ordemProducao = new OrdemProducao();
        ordemProducao.setProduto(produto);

        if (produto.getCliente() == null) {
            throw new Exception("O produto selecionado não possui um cliente vinculado.");
        }

        ordemProducao.setCliente(produto.getCliente());

        ordemProducao.setFuncionario(funcionario);
        ordemProducao.setQtdPedida(dto.qtdPedida());
        ordemProducao.setStatus(OrdemProducao.StatusOrdem.PENDENTE);
        ordemProducao.setDataAbertura(LocalDateTime.now());
        ordemProducao.setAtivo(true);

        return ordemProducaoRepository.save(ordemProducao);
    }


    @Transactional(rollbackFor = Exception.class)
    public OrdemProducao finalizarOrdem(Long id) throws Exception {
        OrdemProducao ordemProducao = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de Produção não encontrada."));

        if (ordemProducao.getStatus().equals(OrdemProducao.StatusOrdem.CONCLUIDO)) {
            throw new Exception("Esta ordem já foi finalizada.");
        }

        List<FichaTecnica> itensFicha = fichaTecnicaService
                .buscarReceitaProduto(ordemProducao.getProduto().getId());

        if (itensFicha.isEmpty()) {
            throw new Exception("Produto não possui uma Ficha Técnica cadastrada.");
        }

        for (FichaTecnica ficha: itensFicha) {
            Double areaAdesivo = (ficha.getAltura() * ficha.getComprimento()) / 10000;

            double consumoSubstrato = ordemProducao.getQtdPedida() * areaAdesivo;

            if (ficha.getTipoAdesivo() == FichaTecnica.TipoAdesivo.RESINADO && ficha.getQtdResina() != null) {
                Double consumoResina =  ficha.getQtdResina() * ordemProducao.getQtdPedida();

                //TODO implementar baixa da resina
            }

            insumoService.baixarEstoque(ficha.getInsumo().getId(), consumoSubstrato);

            MovimentacaoEstoque movInsumo = new MovimentacaoEstoque();
            movInsumo.setInsumo(ficha.getInsumo());
            movInsumo.setQuantidade(-consumoSubstrato);
            movInsumo.setValorUnitario(ordemProducao.getProduto().getValorUnitario());
            movInsumo.setTipo(MovimentacaoEstoque.TipoMovimentacao.SAIDA_INSUMO);
            movInsumo.setObservacao("Consumo automático para Ordem de Produção #" + ordemProducao.getId());

            movimentacaoService.registar(movInsumo);
        }

        Double precoVenda = ordemProducao.getProduto().getValorUnitario();

        produtoService.aumentarQuantidade(ordemProducao.getProduto().getId(), ordemProducao.getQtdPedida());

        MovimentacaoEstoque movProduto = new MovimentacaoEstoque();
        movProduto.setProduto(ordemProducao.getProduto());
        movProduto.setQuantidade(ordemProducao.getQtdPedida().doubleValue());
        movProduto.setTipo(MovimentacaoEstoque.TipoMovimentacao.ENTRADA_PRODUTO);
        movProduto.setValorUnitario(precoVenda);

        movimentacaoService.registar(movProduto);

        ordemProducao.setStatus(OrdemProducao.StatusOrdem.CONCLUIDO);
        ordemProducao.setDataConclusao(LocalDateTime.now());

        return ordemProducaoRepository.save(ordemProducao);
    }

    @Transactional
    public void cancelarOrdem(Long id) throws Exception {
        OrdemProducao ordemProducao = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Ordem de Produção não encontrada."));

        if (ordemProducao.getStatus().equals(OrdemProducao.StatusOrdem.CONCLUIDO)) {
            throw new Exception("Não é possível cancelar uma ordem já concluída.");
        }

        ordemProducao.setStatus(OrdemProducao.StatusOrdem.CANCELADO);
        ordemProducaoRepository.save(ordemProducao);
    }

}