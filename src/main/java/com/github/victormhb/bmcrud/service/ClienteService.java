package com.github.victormhb.bmcrud.service;

import com.github.victormhb.bmcrud.repository.entity.Cliente;
import com.github.victormhb.bmcrud.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repositorio;

    @Autowired
    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Cliente> listar() {
        return repositorio.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    public void adicionarCliente(Cliente cliente) throws Exception {
        if (cliente.getNome().isEmpty()) {
            throw new Exception("Nome não pode ser vazio");
        }
        if (cliente.getCpf().isEmpty()) {
            throw new Exception("CPF/CNPJ não podem ser vazios");
        }
        if (cliente.getTelefone().isEmpty()) {
            throw new Exception("Telefone não pode ser vazio.");
        }

        repositorio.save(cliente);
    }

    public Cliente atualizarCliente(Cliente cliente) throws Exception {
        if (!repositorio.existsById(cliente.getId())) {
            throw new Exception("Cliente com ID " + cliente.getId() + " não encontrado.");
        }

        return repositorio.save(cliente);
    }

    public boolean deletarPorId(Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return true;
        }

        return false;
    }


}
