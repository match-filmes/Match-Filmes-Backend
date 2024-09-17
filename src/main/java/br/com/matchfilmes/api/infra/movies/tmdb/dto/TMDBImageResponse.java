package br.com.matchfilmes.api.infra.movies.tmdb.dto;

public record TMDBImageResponse(
    String file_path,
    int height,
    int width,
    int aspect_ratio
) {
}
