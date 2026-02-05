package com.github.victormhb.bmadesivos.repository;

import com.github.victormhb.bmadesivos.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
