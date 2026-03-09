package com.conectaobra.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record RefreshToken(@NotEmpty(message = "Refresh Token não pode ser vazio!") @JsonProperty String refreshToken) {
}
