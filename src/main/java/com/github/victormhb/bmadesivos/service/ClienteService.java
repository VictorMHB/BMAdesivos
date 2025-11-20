package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.entity.Endereco;
import com.github.victormhb.bmadesivos.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    public Cliente atualizarClienteParcial(Long id, Map<String, Object> updates) throws Exception {
        Optional<Cliente> clienteOptional = repositorio.findById(id);

        if (clienteOptional.isEmpty()) {
            throw new Exception("Cliente com ID " + id + " não encontrado.");
        }

        Cliente cliente = clienteOptional.get();

        if (updates.containsKey("nome")) {
            String novoNome = (String) updates.get("nome");
            if (novoNome != null && !novoNome.isEmpty()) {
                cliente.setNome(novoNome);
            } else {
                throw new Exception("Nome não pode ser vazio");
            }
        }

        if (updates.containsKey("email")) {
            cliente.setEmail((String) updates.get("email"));
        }

        if (updates.containsKey("telefone")) {
            String novoTel = (String) updates.get("telefone");
            if (novoTel != null && !novoTel.isEmpty()) {
                cliente.setTelefone(novoTel);
            } else {
                throw new Exception("Telefone não pode ser vazio");
            }
        }

        if (updates.containsKey("cpfCnpj")) {
            String novoDoc = (String) updates.get("cpfCnpj");
            validarCpfCnpj(novoDoc);
            cliente.setCpfCnpj(novoDoc);
        }

        if (updates.containsKey("endereco")) {
            Map<String, Object> enderecoMap = (Map<String, Object>) updates.get("endereco");

            Endereco endereco = cliente.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
                cliente.setEndereco(endereco);
            }

            if (enderecoMap.containsKey("rua")) endereco.setRua((String) enderecoMap.get("rua"));
            if (enderecoMap.containsKey("numero")) endereco.setNumero((String) enderecoMap.get("numero"));
            if (enderecoMap.containsKey("bairro")) endereco.setBairro((String) enderecoMap.get("bairro"));
            if (enderecoMap.containsKey("cidade")) endereco.setCidade((String) enderecoMap.get("cidade"));
            if (enderecoMap.containsKey("estado")) endereco.setEstado((String) enderecoMap.get("estado"));
            if (enderecoMap.containsKey("cep")) endereco.setCep((String) enderecoMap.get("cep"));
        }

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
