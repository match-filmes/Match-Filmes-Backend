package br.com.matchfilmes.api.infra.movies.tmdb.dto;

public record TMDBImageDTO(
    String file_path,
    int height,
    int width,
    int aspect_ratio
) {
}
