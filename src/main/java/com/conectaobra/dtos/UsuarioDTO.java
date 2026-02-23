package com.conectaobra.dtos;

import com.conectaobra.models.Role;
import com.conectaobra.models.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UsuarioDTO(
        @NotBlank(message = "Não pode ser vazio") @Length(min = 3, max = 25, message = "Deve ter entre 3 e 25 caracteres") String nome,
        @NotBlank(message = "Não pode ser vazio") @Length(min = 6, max = 50)  String senha,
        @Valid Role role){

    public Usuario mapearParaUsuario(){
        return new Usuario(null, nome, senha, role);
    }
}