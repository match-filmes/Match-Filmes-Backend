package br.com.matchfilmes.api.infra.movies;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.Set;

public interface MoviesAPI {
  MovieDTO getMovie(Long movieId) throws MovieNotFoundException;
  PagedModel<MovieDTO> getPopularMovies(Pageable pageable);
  Set<MovieDTO> getRecommendedMoviesByGenres(Pageable pageable, Long[] genresIds);
  PagedModel<MovieDTO> getRecommendedMovies(Pageable pageable, Long movieId);
  PagedModel<MovieDTO> getSimilarMovies(Pageable pageable, Long movieId);
  PagedModel<MovieDTO> getMovies(Pageable pageable, String query);
  GenreDTO getGenre(Long id);
}
