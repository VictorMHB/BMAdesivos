package com.github.victormhb.bmadesivos.dto.ordem;

import com.github.victormhb.bmadesivos.enums.StatusOrdem;

public record OrdemStatusUpdateDTO(
        StatusOrdem novoStatus
) { }
