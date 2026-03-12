package com.github.victormhb.bmadesivos.dto.funcionario;


import com.github.victormhb.bmadesivos.enums.Cargo;

public record FuncionarioDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        Cargo cargo
) {
}
