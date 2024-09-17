package br.com.matchfilmes.api.dtos;

import java.util.List;

public record MovieDTO(
    String title,
    String description,
    String banner,
    List<GenreDTO> genres,
    Long id
) {
}
