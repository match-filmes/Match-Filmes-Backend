package br.com.matchfilmes.api.dtos;

public record AuthenticationResponseDTO(
    String token,
    UserDTO user
) {
}