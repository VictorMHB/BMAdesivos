package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.dto.cliente.ClienteDTO;
import com.github.victormhb.bmadesivos.dto.cliente.ClienteUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:5173",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("todos")
    public List<Cliente> listarClientes(){
        return clienteService.listar();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/novo")
    public ResponseEntity<?> criarCliente(@RequestBody ClienteDTO dto) {
        try {
            clienteService.adicionarCliente(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/editar/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody ClienteUpdateDTO dto) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarCliente(id, dto);
            return ResponseEntity.ok(clienteAtualizado); //Retorna 200
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); //Retorna 404 Not Found
        }
    }

    @DeleteMapping("apagar/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        try {
            clienteService.deletarPorId(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
