package com.conectaobra.controllers;

import com.conectaobra.dtos.GuiaDTO;
import com.conectaobra.models.Cliente;
import com.conectaobra.models.Guia;
import com.conectaobra.services.GuiaService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/guias")
@Data
public class GuiaController {

    private final GuiaService guiaService;

    @GetMapping
    public List<Guia> obterGuias(
            @RequestParam(value = "local", required = false) String local,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "nomeCliente", required = false) String nomeCliente
            ){
        return guiaService.obterGuiaPorParametros(nome, status, nomeCliente, local);
    }

    @GetMapping("/{nome}")
    public Optional<Guia> obterGuia(@RequestParam(required = false, name ="nome") String nome){
        return guiaService.obterGuiaPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<Void> salvarGuia(@RequestBody @Valid GuiaDTO guiaDTO){

        Optional<Cliente> cliente = this.guiaService.guiaEstaValida(guiaDTO);

        Guia guia = guiaDTO.mapearParaGuia(cliente.get());
        guiaService.salvarGuia(guia);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guia.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

}
