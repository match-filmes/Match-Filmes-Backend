package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBMovieResponse(
    String title,
    String overview,
    Set<TMDBGenreResponse> genres,
    String poster_path,
    Long id
) {
}
