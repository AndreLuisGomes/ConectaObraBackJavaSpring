package com.conectaobra.services;

import com.conectaobra.dtos.RefreshToken;
import com.conectaobra.dtos.RefreshTokenResponse;
import com.conectaobra.exceptions.ErroJWTException;
import com.conectaobra.exceptions.TokenExpiradoException;
import com.conectaobra.exceptions.UsuarioNaoEncontradoException;
import com.conectaobra.models.Usuario;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenService {

    // Dependências \\

    private final String API_NAME;

    private static final long ACESS_TOKEN_EXP = 180L;
    private static final long REFRESH_TOKEN_EXP = 86400L;

    private final RSAPublicKey rsaPublicKey;

    private final JwtEncoder jwtEncoder;
    private final UsuarioService usuarioService;

    // JWT Beans \\

    @Bean
    public final long AUTH_REFRESH_TOKEN(){
        return 86400L;
    }

    @Bean
    public final long AUTH_TOKEN(){
        return 180L;
    }

    @Bean
    public final String TOKEN_TYPE(){
        return "Bearer ";
    }

    // Método para validar RefreshToken \\

    public RefreshTokenResponse validarJWT(RefreshToken token){
           try{
               SignedJWT signedJWT = SignedJWT.parse(token.refreshToken());
               JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);
               System.out.println("Token foi verificado. Token: " + token.refreshToken());
               if(!signedJWT.verify(verifier)){
                   System.out.println("Token nao passou no teste de verificacao");
                   throw new ErroJWTException("Token Assinado Errôneamente!");
               }

               Instant expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();

               String nomeUsuario = signedJWT.getJWTClaimsSet().getSubject();
               Usuario usuario =  this.usuarioService.obterPorId(UUID.fromString(nomeUsuario)).orElseThrow(
                       () -> new UsuarioNaoEncontradoException("Usuário não encontrado!"));

               if (expirationTime.isAfter(Instant.now()) && (usuario != null)){
                   System.out.println("Token esta sendo gerado: \n");
                   long refresh_exp = expirationTime.getEpochSecond() - Instant.now().getEpochSecond();
                   refresh_exp = Math.max(0, refresh_exp);
                   return new RefreshTokenResponse(
                           this.gerarJWT(
                                   usuario,
                                   AUTH_TOKEN()
                           ).getTokenValue(),
                           this.gerarJWT(
                                   usuario,
                                   refresh_exp
                           ).getTokenValue()
                   );
               }else{
                    throw new TokenExpiradoException("Token expirado!");
               }
           } catch (ParseException | JOSEException e) {
               throw new ErroJWTException("Token com assinatura inválida ou inválido!");
           }
    }

    // Método para gerar JWT \\

    public Jwt gerarJWT(Usuario usuario, long exp){
        System.out.println(Instant.now());

        Instant now = Instant.now();

        String scope = usuario.getRole().getNome();

        // Claims: são os "componentes" que o JwtEncoder aceita para sua construção \\
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(this.API_NAME)
                .subject(usuario.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(exp))
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}