package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.movimentacao.MovimentacaoDTO;
import com.github.victormhb.bmadesivos.entity.MovimentacaoEstoque;
import com.github.victormhb.bmadesivos.service.MovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
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

    @PostMapping("/ajuste")
    public ResponseEntity<?> ajuste(@RequestBody MovimentacaoDTO dto) {
        try {
            movimentacaoService.realizarAjuste(dto);
            return ResponseEntity.ok("Ajuste realizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
