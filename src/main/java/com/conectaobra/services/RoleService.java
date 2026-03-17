package com.conectaobra.services;

import com.conectaobra.models.Role;
import com.conectaobra.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    // Dependências \\

    private final RoleRepository roleRepository;

    // Metódos para obter \\

    public List<Role> obterTodas(){return this.roleRepository.findAll();};

    public Optional<Role> obterPorId(Long id){
        return this.roleRepository.findById(id);
    }

    public Optional<Role> obterPorNome(String nome){
        return this.roleRepository.findByNome(nome);
    }

    // Métodos para salvar \\

    @Transactional
    public Role salvarRole(Role role){
        return this.roleRepository.save(role);
    }
}
