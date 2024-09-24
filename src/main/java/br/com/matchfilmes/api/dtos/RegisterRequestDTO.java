package br.com.matchfilmes.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank(message = "O nome completo é obrigatório.")
    String fullName,

    @NotBlank(message = "O usuário é obrigatório.")
    String username,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail é inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    String password
) {
}