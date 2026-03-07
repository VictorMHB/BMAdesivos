package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioDTO;
import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioUpdateDTO;
import com.github.victormhb.bmadesivos.dto.funcionario.SenhaUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("todos")
    public List<Funcionario> listarFuncionarios(){
        return funcionarioService.listar();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getFuncionarioPorId(@PathVariable Long id) {
        try {
            Funcionario funcionario = funcionarioService.buscarPorId(id);
            return ResponseEntity.ok(funcionario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> cadastrarFuncionario(@RequestBody FuncionarioDTO dto) throws Exception {
        try {
            String senhaProvisoria = funcionarioService.adicionarFuncionario(dto);
            return ResponseEntity.ok("Funcionário cadastrado com sucesso! Senha temporária: " + senhaProvisoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<?> atualizarFuncionario(@PathVariable Long id, @RequestBody FuncionarioUpdateDTO dto) {
        try {
            Funcionario funcionarioAtualizado = funcionarioService.atualizarFuncionario(id, dto);
            return ResponseEntity.ok(funcionarioAtualizado); //Retorna 200
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); //Retorna 404 Not Found
        }
    }

    @PatchMapping("/{id}/alterar-senha")
    public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody SenhaUpdateDTO dto) {
        try {
            funcionarioService.alterarSenha(id, dto);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("apagar/{id}")
    public ResponseEntity<?> deletarFuncionario(@PathVariable Long id) {
        try {
            funcionarioService.deletarFuncionario(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
