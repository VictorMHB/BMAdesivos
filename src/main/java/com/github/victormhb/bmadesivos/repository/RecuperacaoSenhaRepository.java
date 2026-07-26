package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.RecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Long> {
    Optional<RecuperacaoSenha> findByTokenHash(String tokenHash);
}
