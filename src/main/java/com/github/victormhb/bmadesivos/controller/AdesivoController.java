package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoDTO;
import com.github.victormhb.bmadesivos.dto.adesivo.AdesivoUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import com.github.victormhb.bmadesivos.service.AdesivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adesivos")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AdesivoController {

    private final AdesivoService adesivoService;

    @Autowired
    public AdesivoController(AdesivoService adesivoService) {
        this.adesivoService = adesivoService;
    }

    @GetMapping
    public List<Adesivo> listar() {
        return adesivoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarAdesivoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(adesivoService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> adicionarAdesivo(@RequestBody AdesivoDTO dto) {
        try {
            return ResponseEntity.ok(adesivoService.adicionarAdesivo(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<?> atualizarAdesivo(@PathVariable Long id, @RequestBody AdesivoUpdateDTO dto) {
        try {
            return ResponseEntity.ok(adesivoService.atualizarAdesivo(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> deletarAdesivo(@PathVariable Long id) {
        try {
            adesivoService.deletarAdesivo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}