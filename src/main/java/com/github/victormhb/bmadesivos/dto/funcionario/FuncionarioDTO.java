package com.github.victormhb.bmadesivos.dto.funcionario;

import com.github.victormhb.bmadesivos.entity.Funcionario;

public record FuncionarioDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        Funcionario.Cargo cargo
) {
}
