package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioDTO;
import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioUpdateDTO;
import com.github.victormhb.bmadesivos.dto.funcionario.SenhaUpdateDTO;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository  funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CPFValidator validarCpf = new CPFValidator();

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder =  passwordEncoder;
    }

    public List<Funcionario> listar() {
        Sort ordenacao = Sort.by(Sort.Direction.DESC, "ativo")
                .and(Sort.by(Sort.Direction.ASC, "nome"));

        return funcionarioRepository.findAll(ordenacao);
    }

    public Funcionario buscarPorId(Long id) throws Exception {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Funcionário com a ID: " + id + " não encontrado."));
    }

    @Transactional
    public String adicionarFuncionario(FuncionarioDTO dto) throws Exception {
        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new Exception("Nome não pode ser vazio");
        }

        String nomeTratado = dto.nome().trim();

        if (nomeTratado.length() < 3) {
            throw new Exception("Nome deve ter no mínimo 3 caracteres.");
        }

        if (!nomeTratado.matches("[\\p{L} ]+")) {
            throw new Exception("Nome deve conter apenas letras.");
        }

        validarCpf(dto.cpf());

        String senhaTemp = gerarSenhaTemp();

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nomeTratado);
        funcionario.setCpf(dto.cpf() != null ? dto.cpf().trim() : null);
        funcionario.setEmail(dto.email() != null ? dto.email().trim() : null);
        funcionario.setCargo(dto.cargo());
        funcionario.setAtivo(true);
        funcionario.setTrocarSenha(true);

        String senhaCriptografada = passwordEncoder.encode(senhaTemp);
        funcionario.setSenha(senhaCriptografada);

        funcionarioRepository.save(funcionario);

        return senhaTemp;
    }

    @Transactional
    public Funcionario atualizarFuncionario(Long id, FuncionarioUpdateDTO dto) throws Exception {
        Funcionario funcionario = buscarPorId(id);

        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String nomeTratado = dto.getNome().trim();

            if (nomeTratado.length() < 3) {
                throw new Exception("Nome deve ter no mínimo 3 caracteres.");
            }

            if (!nomeTratado.matches("[\\p{L} ]+")) {
                throw new Exception("Nome deve conter apenas letras.");
            }

            funcionario.setNome(nomeTratado);
        }

        if (dto.getEmail() != null) {
            funcionario.setEmail(dto.getEmail().trim());
        }

        if (dto.getCpf() != null && !dto.getCpf().trim().isEmpty()) {
            validarCpf(dto.getCpf().trim());
            funcionario.setCpf(dto.getCpf().trim());
        }

        if (dto.getAtivo() != null) {
            funcionario.setAtivo(dto.getAtivo());
        }

        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void alterarSenha(Long id, SenhaUpdateDTO dto) throws Exception {
        Funcionario funcionario = buscarPorId(id);

        if (dto.senhaAtual() == null || !passwordEncoder.matches(dto.senhaAtual(), funcionario.getSenha())) {
            throw new Exception("Senha atual incorreta.");
        }

        if (dto.novaSenha() == null || dto.novaSenha().length() < 8) {
            throw new Exception("A nova senha deve ter no mínimo 8 caracteres.");
        }

        funcionario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        funcionario.setTrocarSenha(false);

        funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void deletarFuncionario(Long id) throws Exception {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(false);

        funcionarioRepository.save(funcionario);
    }

    public String gerarSenhaTemp() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        CharacterRule lowerCase = new CharacterRule(EnglishCharacterData.LowerCase, 1);
        CharacterRule upperCase = new CharacterRule(EnglishCharacterData.UpperCase, 1);
        CharacterRule digit = new CharacterRule(EnglishCharacterData.Digit, 1);

        return passwordGenerator.generatePassword(8, lowerCase, upperCase, digit);
    }

    private void validarCpf(String cpf) throws Exception {
        try {
                validarCpf.assertValid(cpf);
        } catch (InvalidStateException e) {
            throw new Exception("Documento inválido: " + cpf);
        }
    }


}
