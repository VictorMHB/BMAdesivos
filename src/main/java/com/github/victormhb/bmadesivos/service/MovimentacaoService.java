package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.MovimentacaoDTO;
import com.github.victormhb.bmadesivos.entity.Material;
import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.MovimentacaoEstoqueRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final MaterialService materialService;
    private final ProdutoService produtoService;

    public MovimentacaoService(MovimentacaoEstoqueRepository movimentacaoRepository, MaterialService materialService, ProdutoService produtoService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.materialService = materialService;
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
        movimentacaoEstoque.setTipo(MovimentacaoEstoque.TipoMovimentacao.AJUSTE);
        movimentacaoEstoque.setValorUnitario(dto.valorUnitario());
        movimentacaoEstoque.setObservacao(dto.observacao());

        if (dto.materialId() != null) {
            Material material = materialService.buscarPorId(dto.materialId());
            movimentacaoEstoque.setMaterial(material);
        } else if (dto.produtoId() != null) {
            Produto produto = produtoService.buscarPorId(dto.produtoId());
            movimentacaoEstoque.setProduto(produto);
        }

        movimentacaoRepository.save(movimentacaoEstoque);
    }
}
