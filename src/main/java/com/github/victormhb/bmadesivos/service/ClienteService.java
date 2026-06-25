package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.dto.cliente.ClienteDTO;
import com.github.victormhb.bmadesivos.dto.cliente.ClienteUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Cliente;
import com.github.victormhb.bmadesivos.entity.Endereco;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CPFValidator validarCpf = new CPFValidator();
    private final CNPJValidator validarCnpj = new CNPJValidator();

    @Autowired
    public ClienteService(ClienteRepository repositorio) {
        this.clienteRepository = repositorio;
    }

    public List<Cliente> listar() {
        Sort ordenacao = Sort.by(Sort.Direction.DESC, "ativo")
                .and(Sort.by(Sort.Direction.ASC, "nome"));

        return clienteRepository.findAll(ordenacao);
    }

    public Cliente buscarPorId(Long id) throws Exception {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new Exception("Cliente com a ID: " + id + " não encontrado."));
    }

    @Transactional
    public void adicionarCliente(ClienteDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("Nome não pode ser vazio");
        }

        String nomeTratado = dto.nome().trim();

        if (nomeTratado.length() < 3) {
            throw new Exception("Nome deve ter no mínimo 3 caracteres.");
        }

        if (!nomeTratado.matches("[\\p{L}0-9 \\-.,()/&+']+")) {
            throw new Exception("Nome contém caracteres inválidos.");
        }

        validarCpfCnpj(dto.cpfCnpj());

        Cliente cliente = new Cliente();
        cliente.setNome(nomeTratado);
        cliente.setCpfCnpj(dto.cpfCnpj() != null ? dto.cpfCnpj().trim() : null);
        cliente.setEmail(dto.email() != null ? dto.email().trim() : null);
        cliente.setTelefone(dto.telefone() != null ? dto.telefone().trim() : null);
        cliente.setAtivo(true);

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setRua(dto.endereco().rua() != null ? dto.endereco().rua().trim() : null);
            endereco.setNumero(dto.endereco().numero());
            endereco.setBairro(dto.endereco().bairro() != null ? dto.endereco().bairro().trim() : null);
            endereco.setCidade(dto.endereco().cidade() != null ? dto.endereco().cidade().trim() : null);
            endereco.setEstado(dto.endereco().estado() != null ? dto.endereco().estado().trim() : null);
            endereco.setCep(dto.endereco().cep() != null ? dto.endereco().cep().trim() : null);
            cliente.setEndereco(endereco);
        }

        clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizarCliente(Long id, ClienteUpdateDTO dto) throws Exception {
        Cliente cliente = buscarPorId(id);

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String nomeTratado = dto.getNome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L}0-9 \\-.,()/&+']+")) {
                throw new Exception("Nome contém caracteres inválidos.");
            }

            cliente.setNome(nomeTratado);
        }

        if (dto.getEmail() != null) {
            cliente.setEmail(dto.getEmail().trim());
        }

        if (dto.getCpfCnpj() != null && !dto.getCpfCnpj().isEmpty()) {
            validarCpfCnpj(dto.getCpfCnpj().trim());
            cliente.setCpfCnpj(dto.getCpfCnpj().trim());
        }

        if (dto.getTelefone() != null && !dto.getTelefone().isEmpty()) {
            cliente.setTelefone(dto.getTelefone().trim());
        }

        if (dto.getAtivo() != null) {
            cliente.setAtivo(dto.getAtivo());
        }

        if (dto.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco() != null ? cliente.getEndereco() : new Endereco();

            if (dto.getEndereco().rua() != null) { endereco.setRua(dto.getEndereco().rua().trim()); }
            if (dto.getEndereco().numero() != null) { endereco.setNumero(dto.getEndereco().numero()); }
            if (dto.getEndereco().bairro() != null) { endereco.setBairro(dto.getEndereco().bairro().trim()); }
            if (dto.getEndereco().cidade() != null) { endereco.setCidade(dto.getEndereco().cidade().trim()); }
            if (dto.getEndereco().estado() != null) { endereco.setEstado(dto.getEndereco().estado().trim()); }
            if (dto.getEndereco().cep() != null) { endereco.setCep(dto.getEndereco().cep().trim()); }

            cliente.setEndereco(endereco);
        }

        return clienteRepository.save(cliente);
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

    @Transactional
    public void deletarCliente(Long id) throws Exception {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }


}
