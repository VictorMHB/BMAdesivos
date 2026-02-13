package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import com.github.victormhb.bmadesivos.repository.MaterialRepository;
import com.github.victormhb.bmadesivos.repository.OrdemProducaoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final ProdutoService produtoService;
    private final MaterialRepository materialRepository;
    private final FichaTecnicaService fichaTecnicaService;
    private final FuncionarioRepository funcionarioRepository;
    private final MaterialService materialService;

    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                ProdutoService produtoService,
                                MaterialRepository materialRepository,
                                FichaTecnicaService fichaTecnicaService,
                                FuncionarioRepository funcionarioRepository, MaterialService materialService)
    {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.produtoService = produtoService;
        this.materialRepository = materialRepository;
        this.fichaTecnicaService = fichaTecnicaService;
        this.funcionarioRepository = funcionarioRepository;
        this.materialService = materialService;
    }

    @Transactional
    public OrdemProducao abrirOrdem(OrdemProducaoDTO dto) throws Exception {
        Produto produto = produtoService.buscarPorId(dto.produtoId());

        Funcionario funcionario = funcionarioRepository.findById(dto.funcionarioId())
                .orElseThrow(() -> new Exception("Funcionário não encontrado."));

        OrdemProducao ordemProducao = new OrdemProducao();
        ordemProducao.setProduto(produto);
        ordemProducao.setFuncionario(funcionario);
        ordemProducao.setQuantidade(dto.quantidade());
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
            throw new Exception("Produto sem Ficha Técnica cadastrada.");
        }

        for (FichaTecnica t: itensFicha) {
            Double qtdConsumida = t.getQtdNecessaria() * ordemProducao.getQuantidade();
            materialService.baixarEstoque(t.getMaterial().getId(), qtdConsumida);
        }

        produtoService.aumentarEstoque(ordemProducao.getProduto().getId(), ordemProducao.getQuantidade());

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

    public List<OrdemProducao> listarTodas() {
        return ordemProducaoRepository.findAll();
    }

}