package com.conectaobra.controllers;

import com.conectaobra.dtos.*;
import com.conectaobra.exceptions.NomeIndisponivelException;
import com.conectaobra.exceptions.RoleInexistenteException;
import com.conectaobra.exceptions.UsuarioSenhaInvalidosException;
import com.conectaobra.models.Role;
import com.conectaobra.models.Usuario;
import com.conectaobra.services.RoleService;
import com.conectaobra.services.TokenService;
import com.conectaobra.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@EnableMethodSecurity
public class AuthController {

    // Dependências \\

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    private final RoleService roleService;

    // JWT Constantes \\

    public final long AUTH_TOKEN;
    public final long AUTH_REFRESH_TOKEN;
    public final String TOKEN_TYPE = "Bearer ";

    // Método de login \\

    @PostMapping("refresh-token")
    ResponseEntity<Object> refreshToken(@RequestBody @Valid RefreshToken refreshToken){
        System.out.println("AuthController -> refreshToken : Passando pelo refreshToken");
        RefreshTokenResponse authToken = this.tokenService.validarJWT(refreshToken, AUTH_TOKEN);
        return authToken != null ? ResponseEntity.ok().body(authToken) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Deu errado" + authToken);
    }

    @PostMapping("logar")
    ResponseEntity<Object> logar(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws Exception{

        UsuarioLoginDTO usuarioLogin = new UsuarioLoginDTO(usuarioLoginDTO.nome().trim(), usuarioLoginDTO.senha());

        // Verificar se os dados batem \\
        Optional<Usuario> usuarioDoBanco = this.usuarioService.obterPorNome(usuarioLogin.nome());

        if(usuarioDoBanco.isPresent() &&
                (usuarioDoBanco.get().getNome().equals(usuarioLogin.nome())) &&
                (this.usuarioService.loginEstaCorreto(usuarioLogin, passwordEncoder))
        ){
            return ResponseEntity.ok().body(
                    new AuthResponse(usuarioLogin.nome(),
                    TOKEN_TYPE,
                    (int) AUTH_TOKEN,
                    this.tokenService.gerarJWT(usuarioDoBanco.get(), AUTH_TOKEN).getTokenValue(),
                    this.tokenService.gerarJWT(usuarioDoBanco.get(), AUTH_REFRESH_TOKEN).getTokenValue(),
                    usuarioDoBanco.get().getRole().getNome()
                    ));
        }
        throw new UsuarioSenhaInvalidosException("Usuário ou senha inválidos!");
    }

    // Método de registrar \\

    @PostMapping("registrar")
    ResponseEntity<Object> registrar(@RequestBody @Valid UsuarioDTO usuarioDTO){
        Optional<Usuario> usuario = this.usuarioService.obterPorNome(usuarioDTO.nome().trim());
        Optional<Role> role = this.roleService.obterPorId(usuarioDTO.role().getId());

        if(role.isEmpty()){
            throw new RoleInexistenteException("Role inválida ou não existe!", "role.id");
        }
        if(usuario.isEmpty()){
            Usuario novoUsuario = usuarioDTO.mapearParaUsuario();
            novoUsuario.setRole(role.get());
            System.out.println(role);
            novoUsuario.setSenha(this.passwordEncoder.encode(novoUsuario.getSenha()));
            this.usuarioService.salvarUsuario(novoUsuario);
            return ResponseEntity.ok().body(
                    new AuthResponse(
                            novoUsuario.getNome(),
                            TOKEN_TYPE,
                            (int) AUTH_TOKEN,
                            this.tokenService.gerarJWT(novoUsuario, AUTH_TOKEN).getTokenValue(),
                            this.tokenService.gerarJWT(novoUsuario, AUTH_REFRESH_TOKEN).getTokenValue(),
                            novoUsuario.getRole().getNome()));
        }
        throw new NomeIndisponivelException("Nome indisponível!", "nome");
    }
}