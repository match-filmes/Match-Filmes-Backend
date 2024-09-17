package br.com.matchfilmes.api.dtos;

import java.util.List;

public record MovieDTO(
    Long id,
    String title,
    String description,
    Double voteAverage,
    List<GenreDTO> genres,
    ImagesDTO images
) {
}
