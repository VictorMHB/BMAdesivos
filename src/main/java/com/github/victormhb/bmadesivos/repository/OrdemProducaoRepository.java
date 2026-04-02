package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import com.github.victormhb.bmadesivos.enums.StatusOrdem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {
    List<OrdemProducao> findByArquivadaFalseAndStatusNot(StatusOrdem status, Sort sort);
    List<OrdemProducao> findByArquivadaTrueOrStatus(StatusOrdem status, Sort sort);
}
