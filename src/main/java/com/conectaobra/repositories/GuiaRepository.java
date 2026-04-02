package com.conectaobra.repositories;

import com.conectaobra.models.Guia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GuiaRepository extends JpaRepository<Guia, UUID> {

    Optional<Guia> findById(UUID uuid);

    Optional<Guia> findByNome(String nome);

    Optional<Guia> findByNomeAndClienteId(String nome, UUID clienteId);
}
