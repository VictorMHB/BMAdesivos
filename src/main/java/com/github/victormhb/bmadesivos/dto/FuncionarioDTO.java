package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.Funcionario;

public record FuncionarioDTO(
        Long id,
        String nome,
        String email,
        Funcionario.Cargo cargo
) {
}
