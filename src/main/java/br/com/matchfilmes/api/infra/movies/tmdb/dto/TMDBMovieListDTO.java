package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBMovieListDTO(
    int page,
    Set<TMDBMovieDTO> results,
    int total_pages,
    int total_results
) {
}
