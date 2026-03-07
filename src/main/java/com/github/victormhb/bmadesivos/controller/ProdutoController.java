package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.ProdutoDTO;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            Produto produto = produtoService.buscarPorId(id);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> criar(@RequestBody ProdutoDTO dto){
        try {
            Produto novoProduto = produtoService.adicionarProduto(dto);
            return ResponseEntity.ok(novoProduto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
