package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.dto.cliente.ClienteDTO;
import com.github.victormhb.bmadesivos.dto.cliente.ClienteUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.entity.Endereco;
import com.github.victormhb.bmadesivos.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        Sort ordenacao = Sort.by(Sort.Direction.DESC, "ativo")
                .and(Sort.by(Sort.Direction.ASC, "nome"));

        return repositorio.findAll(ordenacao);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    public void adicionarCliente(ClienteDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new Exception("Nome não pode ser vazio");
        }

        validarCpfCnpj(dto.cpfCnpj());

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone());
        cliente.setAtivo(true);

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setRua(dto.endereco().rua());
            endereco.setNumero(dto.endereco().numero());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setEstado(dto.endereco().estado());
            endereco.setCep(dto.endereco().cep());
            cliente.setEndereco(endereco);
        }

        repositorio.save(cliente);
    }

    public Cliente atualizarCliente(Long id, ClienteUpdateDTO dto) throws Exception {
        Cliente cliente = repositorio.findById(id)
                .orElseThrow(() -> new Exception("Cliente com ID " + id + " não foi encontrado."));

        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            cliente.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            cliente.setEmail(dto.getEmail());
        }

        if (dto.getCpfCnpj() != null && !dto.getCpfCnpj().isEmpty()) {
            validarCpfCnpj(dto.getCpfCnpj());
            cliente.setCpfCnpj(dto.getCpfCnpj());
        }

        if (dto.getTelefone() != null && !dto.getTelefone().isEmpty()) {
            cliente.setTelefone(dto.getTelefone());
        }

        if (dto.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco() != null ? cliente.getEndereco() : new Endereco();

            if (dto.getEndereco().rua() != null) { endereco.setRua(dto.getEndereco().rua()); }
            if (dto.getEndereco().numero() != null) { endereco.setNumero(dto.getEndereco().numero()); }
            if (dto.getEndereco().bairro() != null) { endereco.setBairro(dto.getEndereco().bairro()); }
            if (dto.getEndereco().cidade() != null) { endereco.setCidade(dto.getEndereco().cidade()); }
            if (dto.getEndereco().estado() != null) { endereco.setEstado(dto.getEndereco().estado()); }
            if (dto.getEndereco().cep() != null) { endereco.setCidade(dto.getEndereco().cep()); }

            cliente.setEndereco(endereco);
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
