package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}
