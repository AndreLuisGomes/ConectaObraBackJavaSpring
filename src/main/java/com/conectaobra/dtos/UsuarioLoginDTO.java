package com.conectaobra.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioLoginDTO(
        @NotBlank(message = "Não pode ser vazio") String nome,
        @NotNull(message = "Não pode ser vazio") String senha) {
}