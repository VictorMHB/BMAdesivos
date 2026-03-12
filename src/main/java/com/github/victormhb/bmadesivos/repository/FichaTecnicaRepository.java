package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.FichaTecnica;
import com.github.victormhb.bmadesivos.entity.Adesivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {
    List<FichaTecnica> findByAdesivo(Adesivo adesivo);
}
