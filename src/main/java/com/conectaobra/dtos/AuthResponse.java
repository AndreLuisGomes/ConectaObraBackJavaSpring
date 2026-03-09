package com.conectaobra.dtos;

public record AuthResponse(String nome,
                           String tokenType,
                           int expiresIn,
                           String accessToken,
                           String refreshToken,
                           String role) {
}
