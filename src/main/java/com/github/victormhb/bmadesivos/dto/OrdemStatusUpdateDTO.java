package com.github.victormhb.bmadesivos.dto;

import com.github.victormhb.bmadesivos.enums.StatusOrdem;

public record OrdemStatusUpdateDTO(
        StatusOrdem novoStatus
) { }
