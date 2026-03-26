package com.conectaobra.dtos;

import com.conectaobra.models.Guia;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record GuiaDTO(
                        @NotBlank @Length(min = 3, max = 40) String nome,
                        @NotBlank String status,
                        @NotBlank String local,
                        @NotBlank UUID nomeCliente) {

    public Guia mapearParaGuia(){
        Guia guia = new Guia();
        guia.setLocal(local);
        guia.setNome(nome);
        guia.setLocal(local);
        guia.setClienteId(nomeCliente);
        return guia;
    }
}
