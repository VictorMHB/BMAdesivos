package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import com.github.victormhb.bmadesivos.service.OrdemProducaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producao")
@CrossOrigin(origins = "http://localhost:5173")
public class OrdemProducaoController {

    private final OrdemProducaoService ordemProducaoService;

    @Autowired
    public OrdemProducaoController(OrdemProducaoService ordemProducaoService) {
        this.ordemProducaoService = ordemProducaoService;
    }

    @GetMapping("/todas")
    public List<OrdemProducao> listarTodas() {
        return ordemProducaoService.listarTodas();
    }

    @PostMapping("/abrir")
    public ResponseEntity<?> abrirOrdem(@RequestBody OrdemProducaoDTO dto) {
        try {
            OrdemProducao novaOrdem = ordemProducaoService.abrirOrdem(dto);
            return ResponseEntity.ok(novaOrdem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/finalizar/{id}")
    public ResponseEntity<?> fecharOrdem(@PathVariable Long id) {
        try {
            OrdemProducao ordemFinalizada = ordemProducaoService.finalizarOrdem(id);
            return ResponseEntity.ok(ordemFinalizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> cancelarOrdem(@PathVariable Long id) {
        try {
            ordemProducaoService.cancelarOrdem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
