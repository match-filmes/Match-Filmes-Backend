package br.com.matchfilmes.api.dtos;

import java.util.UUID;

public record UserDTO(
    UUID id,
    String fullName,
    String username,
    String email
) {
}
