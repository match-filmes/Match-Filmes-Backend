package br.com.matchfilmes.api.infra.movies.tmdb;

import java.util.Set;

public record TMDBMovieResponse(
    String title,
    String overview,
    Set<TMDBGenreResponse> genres
) {
}
