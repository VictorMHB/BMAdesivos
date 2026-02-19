package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.repository.MovimentacaoEstoqueRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public MovimentacaoService(MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
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
}
