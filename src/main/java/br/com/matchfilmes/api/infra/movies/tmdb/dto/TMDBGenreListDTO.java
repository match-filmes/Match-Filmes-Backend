package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBGenreListDTO(
    Set<TMDBGenreDTO> genres
) {
}
