package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.insumo.InsumoDTO;
import com.github.victormhb.bmadesivos.dto.insumo.InsumoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Insumo;
import com.github.victormhb.bmadesivos.service.InsumoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insumos")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @GetMapping
    public List<Insumo> listar() {
        return insumoService.listar();
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
    public ResponseEntity<?> adicionarInsumo(@RequestBody InsumoDTO dto) {
        try {
            return ResponseEntity.ok(insumoService.adicionarInsumo(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<?> atualizarInsumo(@PathVariable Long id, @RequestBody InsumoUpdateDTO dto) {
        try {
            return ResponseEntity.ok(insumoService.atualizarInsumo(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> deletarInsumo(@PathVariable Long id) {
        try{
            insumoService.deletarInsumo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
