package com.conectaobra.dtos;

import com.conectaobra.models.Cliente;
import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(@NotBlank String nome,
                         @NotBlank String contato,
                         @NotBlank String localizacaoSede) {

    public Cliente mapearParaCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome.trim());
        cliente.setContato(this.contato.trim());
        cliente.setLocalizacaoSede(this.localizacaoSede.trim());
        return cliente;
    }
}
