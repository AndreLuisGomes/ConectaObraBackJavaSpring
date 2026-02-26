package com.conectaobra.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioLoginDTO(
        @NotBlank(message = "Campo obrigatório!") String nome,
        @NotNull(message = "Campo obrigatório!") String senha) {
}