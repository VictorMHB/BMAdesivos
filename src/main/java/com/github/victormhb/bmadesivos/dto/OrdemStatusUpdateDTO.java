package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.entity.OrdemProducao;

public record OrdemStatusUpdateDTO(
        OrdemProducao.StatusOrdem novoStatus
) { }
