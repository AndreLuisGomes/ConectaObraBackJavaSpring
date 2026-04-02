package com.conectaobra.dtos;

import com.conectaobra.models.Cliente;
import com.conectaobra.models.Guia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record GuiaDTO(
                        @NotBlank @Length(min = 3, max = 40) String nome,
                        @NotBlank String status,
                        @NotBlank String clienteId,
                        @NotBlank String local) {

    public Guia mapearParaGuia(Cliente cliente){
        Guia guia = new Guia();
        guia.setNome(nome);
        guia.setStatus(status);
        guia.setCliente(cliente);
        guia.setLocal(local);
        return guia;
    }
}
