package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioDTO;
import com.github.victormhb.bmadesivos.dto.funcionario.FuncionarioUpdateDTO;
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

    public List<Funcionario> listar(Long id) {
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
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new Exception("Nome não pode ser vazio");
        }

        validarCpf(dto.cpf());

        String senhaTemp = gerarSenhaTemp();

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.nome());
        funcionario.setCpf(dto.cpf());
        funcionario.setEmail(dto.email());
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

        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            funcionario.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            funcionario.setEmail(dto.getEmail());
        }

        if (dto.getCpf() != null && !dto.getCpf().isEmpty()) {
            validarCpf(dto.getCpf());
            funcionario.setCpf(dto.getCpf());
        }

        if (dto.getNovaSenha() != null) {
            if (!funcionario.isTrocarSenha()) {
                if (dto.getSenhaAtual() == null || !passwordEncoder.matches(dto.getSenhaAtual(), funcionario.getSenha())) {
                    throw new Exception("Senha atual incorreta.");
                }
            }

            if (dto.getNovaSenha().length() < 8) {
                throw new Exception("Senha deve ter no minimo 8 caracteres.");
            }

            funcionario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
            funcionario.setTrocarSenha(false);
        }

        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void deletarPorId(Long id) throws Exception {
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
