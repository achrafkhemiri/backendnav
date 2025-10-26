package com.example.navire.repository;

import com.example.navire.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByNumero(String numero);
}
