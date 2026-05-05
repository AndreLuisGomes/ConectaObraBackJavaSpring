package com.conectaobra.services;

import com.conectaobra.dtos.GuiaDTO;
import com.conectaobra.exceptions.GuiaInvalidaException;
import com.conectaobra.models.Cliente;
import com.conectaobra.models.Guia;
import com.conectaobra.repositories.GuiaRepository;
import com.conectaobra.repositories.specs.GuiaSpecs;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GuiaService {

    // Dependências \\

    private final GuiaRepository guiaRepository;
    private final ClienteService clienteService;

    // Métodos de verificação \\

    public Optional<Cliente> guiaEstaValida(GuiaDTO guiaDTO){
        Optional<Cliente> cliente = this.clienteService.obterClientePorId(UUID.fromString(guiaDTO.clienteId()));
        if(cliente.isEmpty()){
           throw new GuiaInvalidaException("Cliente não existe!");
        }
        if(this.guiaRepository.findByNomeAndClienteId(guiaDTO.nome(), UUID.fromString(guiaDTO.clienteId())).isPresent()){
            throw new GuiaInvalidaException("Já existe uma guia com este nome para este cliente!");
        }
        Optional<Guia> guia = this.guiaRepository.findByNome(guiaDTO.nome());
        return cliente;
    }

    // Métodos para obter \\

    public List<Guia> obterTodasAsGuias(){
        return this.guiaRepository.findAll();
    }

    public List<Guia> obterGuiaPorParametros(String nome, String status, String nomeCliente, String local){

        Specification<Guia> specs = Specification.where(null);

        if(StringUtils.hasText(nome)){
            specs = specs.and(GuiaSpecs.nomeLike(String.valueOf(nome)));
        }

        if(StringUtils.hasText(status)){
            specs = specs.and(GuiaSpecs.localLike(status));
        }

        if (StringUtils.hasText(String.valueOf(nomeCliente))) {
            specs = specs.and(GuiaSpecs.nomeClienteLike(String.valueOf(nomeCliente)));
        }

        if (StringUtils.hasText(String.valueOf(local))) {
            specs = specs.and(GuiaSpecs.nomeClienteLike(String.valueOf(local)));
        }

        return guiaRepository.findAll();
    }

    public Optional<Guia> obterGuiaPorNome(String nome){
        return guiaRepository.findByNome(nome);
    }

    // Métodos para salvar \\

    @Transactional
    public Guia salvarGuia(Guia guia){
        return guiaRepository.save(guia);
    }

    // Métodos para atualizar \\

    @Transactional
    public Guia atualizarGuia(Guia guia){
        Optional<Guia> guiaEncontrada = guiaRepository.findById(guia.getId());
        if(guiaEncontrada.isPresent()){
            return guiaRepository.save(guia);
        }
        return guia;
    }

    // Métodos para deletar \\

    @Transactional
    public void deletarGuia(UUID uuid){
        guiaRepository.deleteById(uuid);
    }
}
