package br.com.matchfilmes.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "O nome de usuário é obrigatório.")
    String username,
    @NotBlank(message = "A senha é obrigatória.")
    String password,
    boolean rememberUser
) {

}