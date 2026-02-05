package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.OrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {
}
