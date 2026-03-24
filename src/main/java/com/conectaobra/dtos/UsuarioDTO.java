package com.conectaobra.dtos;

import com.conectaobra.models.Role;
import com.conectaobra.models.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UsuarioDTO(
        @NotBlank(message = "Campo obrigatório!") @Length(min = 3, max = 25, message = "Deve ter entre 3 e 25 caracteres") String nome,
        @NotBlank(message = "Campo obrigatório!") @Length(min = 5, max = 50, message = "O comprimento deve ser entre 6 e 50 caracteres!")  String senha,
        @Valid Role role){

    public Usuario mapearParaUsuario(){
        return new Usuario(null, nome, senha, role);
    }
}