package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.MaterialDTO;
import com.github.victormhb.bmadesivos.entity.Material;
import com.github.victormhb.bmadesivos.entity.Produto;
import com.github.victormhb.bmadesivos.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public List<Material> listar() {
        return materialService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        try {
            Material material = materialService.buscarPorId(id);
            return ResponseEntity.ok(material);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> criar(@RequestBody MaterialDTO dto) {
        try {
            return ResponseEntity.ok(materialService.salvar(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody MaterialDTO dto) {
        try {
            return ResponseEntity.ok(materialService.atualizar(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
