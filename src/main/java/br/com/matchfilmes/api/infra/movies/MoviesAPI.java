package br.com.matchfilmes.api.infra.movies;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;

import java.util.Set;

public interface MoviesAPI {
  MovieDTO getMovie(Long movieId);
  Set<MovieDTO> getMovies();
  Set<MovieDTO> getMovies(String query);

  GenreDTO getGenre(Long genreId);
  Set<GenreDTO> getGenres();
  Set<GenreDTO> getGenres(String query);
}
