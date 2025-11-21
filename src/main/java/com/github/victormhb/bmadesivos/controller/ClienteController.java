package com.github.victormhb.bmadesivos.controller;

import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ClienteController {

    private final ClienteService servico;

    @Autowired
    public ClienteController(ClienteService servico) {
        this.servico = servico;
    }

    @PostMapping("novo")
    public void criarCliente(@RequestBody Cliente cliente) throws Exception {
        servico.adicionarCliente(cliente);
    }

    @GetMapping("todos")
    public List<Cliente> listarClientes(){
        return servico.listar();
    }

    @GetMapping("{id}")
    public ResponseEntity<Cliente> getClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = servico.buscarPorId(id);

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get()); //Retorna 200
        } else  {
            return ResponseEntity.notFound().build(); //Retorna 404 Not Found
        }
    }

    @PutMapping("atualizar")
    public ResponseEntity<Cliente> atualizarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = servico.atualizarCliente(cliente);
            return ResponseEntity.ok(clienteAtualizado); //Retorna 200
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); //Retorna 404 Not Found
        }
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<Cliente> atualizarParcialCliente(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Cliente clienteAtualizado = servico.atualizarClienteParcial(id, updates);
            return ResponseEntity.ok(clienteAtualizado); //Retorna 200
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); //Retorna 404 Not Found
        }
    }

    @DeleteMapping("apagar/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        boolean deletado = servico.deletarPorId(id);
        if (deletado) {
            return ResponseEntity.ok().build(); //Retorna 200 OK
        } else  {
            return ResponseEntity.notFound().build(); //Retorna 404 Not Found
        }
    }
}
