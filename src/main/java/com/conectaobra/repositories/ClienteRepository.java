package com.conectaobra.repositories;

import com.conectaobra.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {

    @Override
    void deleteById(UUID uuid);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    List<Cliente> findByContato(String contato);
}
