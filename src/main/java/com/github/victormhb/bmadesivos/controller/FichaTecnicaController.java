package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.adesivo.FichaTecnicaDTO;
import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.service.FichaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adesivos/{adesivoId}/ficha-tecnica")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FichaTecnicaController {

    private final FichaTecnicaService fichaTecnicaService;

    @Autowired
    public FichaTecnicaController(FichaTecnicaService fichaTecnicaService) {
        this.fichaTecnicaService = fichaTecnicaService;
    }

    @GetMapping
    public ResponseEntity<?> listar(@PathVariable Long adesivoId) {
        try {
            List<FichaTecnica> itens = fichaTecnicaService.buscarPorAdesivo(adesivoId);
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> adicionar(@PathVariable Long adesivoId, @RequestBody FichaTecnicaDTO dto) {
        try {
            FichaTecnica itemInsumo = fichaTecnicaService.adicionarInsumoFicha(adesivoId, dto);
            return ResponseEntity.ok(itemInsumo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{insumoId}")
    public ResponseEntity<?> remover(@PathVariable Long insumoId) {
        try {
            fichaTecnicaService.deletarInsumoFicha(insumoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}