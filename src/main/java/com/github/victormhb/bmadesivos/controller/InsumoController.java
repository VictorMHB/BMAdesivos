package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.InsumoDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.service.InsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insumos")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @GetMapping
    public List<Insumo> listar() {
        return insumoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            Insumo insumo = insumoService.buscarPorId(id);
            return ResponseEntity.ok(insumo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> criar(@RequestBody InsumoDTO dto) {
        try {
            return ResponseEntity.ok(insumoService.salvar(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody InsumoDTO dto) {
        try {
            return ResponseEntity.ok(insumoService.atualizar(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
