package com.github.victormhb.bmcrud.service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmcrud.repository.entity.Cliente;
import com.github.victormhb.bmcrud.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repositorio;
    private final CPFValidator validarCpf = new CPFValidator();
    private final CNPJValidator validarCnpj = new CNPJValidator();

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
        if (cliente.getCpfCnpj().isEmpty()) {
            throw new Exception("CPF/CNPJ não podem ser vazios");
        }

        validarCpfCnpj(cliente.getCpfCnpj());

        if (cliente.getTelefone().isEmpty()) {
            throw new Exception("Telefone não pode ser vazio.");
        }

        repositorio.save(cliente);
    }

    public Cliente atualizarCliente(Cliente cliente) throws Exception {
        if (!repositorio.existsById(cliente.getId())) {
            throw new Exception("Cliente com ID " + cliente.getId() + " não encontrado.");
        }

        validarCpfCnpj(cliente.getCpfCnpj());

        return repositorio.save(cliente);
    }

    private void validarCpfCnpj(String cpfCnpj) throws Exception {
        try {
            if (cpfCnpj.length() < 14) {
                validarCpf.assertValid(cpfCnpj);
            } else {
                validarCnpj.assertValid(cpfCnpj);
            }
        } catch (InvalidStateException e) {
            throw new Exception("Documento inválido: " + cpfCnpj);
        }
    }

    public boolean deletarPorId(Long id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return true;
        }

        return false;
    }


}
