package com.conectaobra.controllers;

import com.conectaobra.common.exceptions.NomeIndisponivelException;
import com.conectaobra.common.exceptions.RoleInexistenteException;
import com.conectaobra.common.exceptions.UsuarioSenhaInvalidosException;
import com.conectaobra.dtos.AuthResponse;
import com.conectaobra.dtos.UsuarioDTO;
import com.conectaobra.dtos.UsuarioLoginDTO;
import com.conectaobra.models.Role;
import com.conectaobra.models.Usuario;
import com.conectaobra.services.RoleService;
import com.conectaobra.services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@EnableMethodSecurity
public class AuthController {

    // Dependências \\
    public final String apiName;

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;

    private final UsuarioService usuarioService;
    private final RoleService roleService;

    // JWT Constantes \\

    private final long AUTH_REFRESH_TOKEN = 86400L;
    private final long AUTH_TOKEN = 300L;

    // Método de login \\

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
                    this.gerarJWT(usuarioDoBanco.get(), AUTH_TOKEN).getTokenValue(),
                    this.gerarJWT(usuarioDoBanco.get(), AUTH_REFRESH_TOKEN).getTokenValue(),
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
                    new AuthResponse(novoUsuario.getNome(),
                            this.gerarJWT(novoUsuario, this.AUTH_TOKEN).getTokenValue(),
                            this.gerarJWT(novoUsuario, this.AUTH_REFRESH_TOKEN).getTokenValue(),
                            novoUsuario.getRole().getNome()));
        }
        throw new NomeIndisponivelException("Nome indisponível!", "nome");
    }

    // Método para gerar JWT \\

    private Jwt gerarJWT(Usuario usuario, long exp){
        Instant now = Instant.now();

        String scope = usuario.getRole().getNome();

        // Claims: são os "componentes" que o JwtEncoder aceita para sua construção \\
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(this.apiName)
                .subject(usuario.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(exp))
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}