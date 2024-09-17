package br.com.matchfilmes.api.infra.movies;

import br.com.matchfilmes.api.dtos.MovieDTO;

import java.util.Set;

public interface MoviesAPI {
  MovieDTO getMovie(Long movieId);
  Set<MovieDTO> getPopularMovies();
  Set<MovieDTO> getRecommendedMovies(Long movieId);
  Set<MovieDTO> getSimilarMovies(Long movieId);
  Set<MovieDTO> getMovies(String query);
}
