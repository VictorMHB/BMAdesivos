package com.github.victormhb.bmadesivos.dto.cliente;

public record EnderecoDTO(
        String rua,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep
) { }
