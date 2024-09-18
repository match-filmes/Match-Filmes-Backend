package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieService {
  private final MoviesAPI moviesAPI;

  public MovieDTO findById(Long movieId) {
    return moviesAPI.getMovie(movieId);
  }
}
