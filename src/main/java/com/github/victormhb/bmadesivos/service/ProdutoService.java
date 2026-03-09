package com.github.victormhb.bmadesivos.service;

import com.github.victormhb.bmadesivos.dto.ProdutoDTO;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ClienteService clienteService;

    public ProdutoService(ProdutoRepository repositorio, ClienteService clienteService) {
        this.produtoRepository = repositorio;
        this.clienteService = clienteService;
    }

    public List<Produto> listar(){
        return produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public Produto buscarPorId(Long id) throws Exception {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto com ID: " + id + " não foi encontrado."));
    }

    @Transactional
    public Produto adicionarProduto(ProdutoDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("O nome do produto é obrigatório.");
        }

        String nomeTratado = dto.nome().trim();

        if (nomeTratado.length() < 3) {
            throw new Exception("Nome deve ter no mínimo 3 caracteres.");
        }

        if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
            throw new Exception("Nome contém caracteres inválidos.");
        }

        if (dto.valorUnitario() == null || dto.valorUnitario() <= 0) {
            throw new Exception("O preço de venda deve ser maior que zero.");
        }

        Cliente cliente = clienteService.buscarPorId(dto.clienteId());

        Produto produto = new Produto();
        produto.setNome(nomeTratado);
        produto.setDescricao(dto.descricao() != null ? dto.descricao().trim() : null);
        produto.setValorUnitario(dto.valorUnitario());
        produto.setQuantidade(dto.quantidade() != null ? dto.quantidade() : 0);
        produto.setAtivo(true);
        produto.setCliente(cliente);

        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDTO dto) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto com ID: " + id + " não encontrado."));

        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            String nomeTratado = dto.nome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L}0-9 ]+")) {
                throw new Exception("Nome contém caracteres inválidos.");
            }

            produto.setNome(nomeTratado);
        }
        if (dto.descricao() != null) {
            produto.setDescricao(dto.descricao());
        }
        if (dto.quantidade() != null && dto.quantidade() > 0) {
            produto.setValorUnitario(dto.valorUnitario());
        }
        if (dto.quantidade() != null) {
            produto.setQuantidade(dto.quantidade());
        }

        return produtoRepository.save(produto);
    }

    @Transactional
    public void aumentarQuantidade(Long id, Integer qtdProduzida) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto com ID: " + id + " não encontrado."));

        if (qtdProduzida <= 0) {
            throw new Exception("A quantidade produzida deve ser positiva.");
        }

        produto.setQuantidade(produto.getQuantidade() + qtdProduzida);
        produtoRepository.save(produto);
    }

    @Transactional
    public void deletarProduto(Long id) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado."));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

}
