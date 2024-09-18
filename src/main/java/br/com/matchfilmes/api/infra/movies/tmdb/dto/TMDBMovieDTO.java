package br.com.matchfilmes.api.infra.movies.tmdb.dto;

import java.util.Set;

public record TMDBMovieDTO(
    String title,
    String overview,
    Set<TMDBGenreDTO> genres,
    String poster_path,
    Long id,
    TMDBImagesDTO images,
    Double vote_average
) {
}
