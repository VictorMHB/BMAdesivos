package com.github.victormhb.bmcrud.repository;

import com.github.victormhb.bmcrud.repository.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
