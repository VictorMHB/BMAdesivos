package com.github.victormhb.bmadesivos.service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.github.victormhb.bmadesivos.entity.Funcionario;
import com.github.victormhb.bmadesivos.repository.FuncionarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository  funcionarioRepository;
    private final CPFValidator validarCpf = new CPFValidator();
    private final CNPJValidator validarCnpj = new CNPJValidator();

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> listar(Long id) {
        Sort ordenacao = Sort.by(Sort.Direction.DESC, "ativo")
                .and(Sort.by(Sort.Direction.ASC, "nome"));

        return funcionarioRepository.findAll(ordenacao);
    }

    public Funcionario listarPorId(Long id) throws Exception {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Funcionário com a ID: " + id + " não encontrado."));
    }


}
