package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import com.github.victormhb.bmadesivos.service.OrdemProducaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class OrdemProducaoController {

    private final OrdemProducaoService ordemProducaoService;

    @Autowired
    public OrdemProducaoController(OrdemProducaoService ordemProducaoService) {
        this.ordemProducaoService = ordemProducaoService;
    }

    @GetMapping("/todas")
    public List<OrdemProducao> listar() {
        return ordemProducaoService.listar();
    }

    @GetMapping("/historico")
    public List<OrdemProducao> listarHistorico() {
        return ordemProducaoService.listarHistorico();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordemProducaoService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/nova")
    public ResponseEntity<?> abrir(@RequestBody OrdemProducaoDTO dto) {
        try {
            return ResponseEntity.ok(ordemProducaoService.abrirOrdem(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/avancar")
    public ResponseEntity<?> avancar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordemProducaoService.avancarStatus(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordemProducaoService.finalizarOrdem(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/arquivar")
    public ResponseEntity<?> arquivar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordemProducaoService.arquivarOrdem(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordemProducaoService.cancelarOrdem(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}