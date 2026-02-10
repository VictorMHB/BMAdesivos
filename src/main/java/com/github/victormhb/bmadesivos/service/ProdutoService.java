package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.ProdutoDTO;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository repositorio;

    public ProdutoService(ProdutoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Produto> listarProdutos(){
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public Optional<Produto> buscarProdutoPorId(Long id){
        return repositorio.findById(id);
    }

    public Produto salvar(ProdutoDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isBlank()){
            throw new Exception("O nome do produto é obrigatório.");
        }

        if (dto.precoVenda() == null || dto.precoVenda() <= 0) {
            throw new Exception("O preço de venda deve ser maior que zero.");
        }

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setEstoqueAtual(dto.estoqueAtual() != null ? dto.estoqueAtual() : 0);
        produto.setAtivo(true);

        return repositorio.save(produto);
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoDTO dto) throws Exception {
        Produto produto = repositorio.findById(id)
                .orElseThrow(() -> new Exception("Produto com ID: " + id + " não encontrado."));

        if (dto.nome() != null && !dto.nome().trim().isBlank()) {
            produto.setNome(dto.nome());
        }
        if (dto.descricao() != null) {
            produto.setDescricao(dto.descricao());
        }
        if (dto.precoVenda() != null && dto.precoVenda() > 0) {
            produto.setPrecoVenda(dto.precoVenda());
        }
        if (dto.estoqueAtual() != null) {
            produto.setEstoqueAtual(dto.estoqueAtual());
        }

        return repositorio.save(produto);
    }

    public void aumentarEstoque(Long id, Integer qtdProduzida) throws Exception {
        Produto produto = repositorio.findById(id)
                .orElseThrow(() -> new Exception("Produto com ID: " + id + " não encontrado."));

        if (qtdProduzida <= 0) {
            throw new Exception("A quantidade produzida deve ser positiva.");
        }

        produto.setEstoqueAtual(produto.getEstoqueAtual() + qtdProduzida);
        repositorio.save(produto);
    }

    public void deletar(Long id) throws Exception {
        Produto produto = repositorio.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado."));

        produto.setAtivo(false);
        repositorio.save(produto);
    }

}
