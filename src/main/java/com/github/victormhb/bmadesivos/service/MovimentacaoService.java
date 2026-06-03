package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.movimentacao.MovimentacaoDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.enums.TipoMovimentacao;
import com.github.victormhb.bmadesivos.repository.MovimentacaoEstoqueRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final InsumoService insumoService;
    private final AdesivoService produtoService;

    public MovimentacaoService(MovimentacaoEstoqueRepository movimentacaoRepository, InsumoService insumoService, AdesivoService produtoService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.insumoService = insumoService;
        this.produtoService = produtoService;
    }

    public List<MovimentacaoEstoque> listarHistorico() {
        return movimentacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
    }

    @Transactional
    public void registar(MovimentacaoEstoque movimentacaoEstoque) {
        movimentacaoRepository.save(movimentacaoEstoque);
    }

    public MovimentacaoEstoque buscarPorId(Long id) throws Exception {
        return movimentacaoRepository.findById(id)
                .orElseThrow(() -> new Exception("Movimentação com ID: " + id + " não encontrada."));
    }

    @Transactional
    public void realizarAjuste(MovimentacaoDTO dto) throws Exception {
        MovimentacaoEstoque movimentacaoEstoque = new MovimentacaoEstoque();
        movimentacaoEstoque.setQuantidade(dto.quantidade());
        movimentacaoEstoque.setTipo(TipoMovimentacao.AJUSTE);
        movimentacaoEstoque.setValorUnitario(dto.valorUnitario() != null ? dto.valorUnitario() : 0.0);
        movimentacaoEstoque.setObservacao(dto.observacao());

        if (dto.insumoId() != null) {
            Insumo insumo = insumoService.buscarPorId(dto.insumoId());

            double novoEstoque = insumo.getEstoqueAtual() + dto.quantidade();
            if (novoEstoque < 0)
                throw new Exception("Ajuste resultaria em estoque negativo para " + insumo.getNome());

            insumo.setEstoqueAtual(novoEstoque);
            movimentacaoEstoque.setInsumo(insumo);

        } else if (dto.adesivoId() != null) {
            Adesivo adesivo = produtoService.buscarPorId(dto.adesivoId());
            movimentacaoEstoque.setAdesivo(adesivo);
        }

        movimentacaoRepository.save(movimentacaoEstoque);
    }
}
