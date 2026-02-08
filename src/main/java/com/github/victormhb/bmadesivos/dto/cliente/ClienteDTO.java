package com.github.victormhb.bmadesivos.dto.cliente;

public record ClienteDTO(
        String nome,
        String cpfCnpj,
        String email,
        String telefone,
        EnderecoDTO endereco
) { }
