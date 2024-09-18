package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBImagesDTO(
    Set<TMDBImageDTO> backdrops,
    Set<TMDBImageDTO> logos,
    Set<TMDBImageDTO> posters
) {
}