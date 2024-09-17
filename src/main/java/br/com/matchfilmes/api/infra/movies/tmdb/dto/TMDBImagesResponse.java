package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBImagesResponse(
    Set<TMDBImageResponse> backdrops,
    Set<TMDBImageResponse> logos,
    Set<TMDBImageResponse> posters
) {
}