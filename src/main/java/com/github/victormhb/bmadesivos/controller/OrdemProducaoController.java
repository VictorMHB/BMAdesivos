package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.ordem.OrdemProducaoDTO;
import com.github.victormhb.bmadesivos.dto.ordem.OrdemResponseDTO;
import com.github.victormhb.bmadesivos.service.OrdemProducaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens")
public class OrdemProducaoController {

    private final OrdemProducaoService ordemProducaoService;

    @Autowired
    public OrdemProducaoController(OrdemProducaoService ordemProducaoService) {
        this.ordemProducaoService = ordemProducaoService;
    }

    @GetMapping("/todas")
    public List<OrdemResponseDTO> listar() {
        return ordemProducaoService.listar()
                .stream()
                .map(OrdemResponseDTO::de)
                .toList();
    }

    @GetMapping("/historico")
    public List<OrdemResponseDTO> listarHistorico() {
        return ordemProducaoService.listarHistorico()
                .stream()
                .map(OrdemResponseDTO::de)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.buscarPorId(id)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/nova")
    public ResponseEntity<?> abrir(@RequestBody OrdemProducaoDTO dto) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.abrirOrdem(dto)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/avancar")
    public ResponseEntity<?> avancar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.avancarStatus(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.finalizarOrdem(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/arquivar")
    public ResponseEntity<?> arquivar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.arquivarOrdem(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(OrdemResponseDTO.de(ordemProducaoService.cancelarOrdem(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}