package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.service.MovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping
    public List<MovimentacaoEstoque> listar() {
        return movimentacaoService.listarHistorico();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            MovimentacaoEstoque movimentacaoEstoque = movimentacaoService.buscarPorId(id);
            return ResponseEntity.ok(movimentacaoEstoque);
        }  catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
