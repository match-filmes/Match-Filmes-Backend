package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.models.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MovieService {
  private final MoviesAPI moviesAPI;
  private final UserAlgorithmService userAlgorithmService;

  public MovieDTO findById(Long movieId, User user) {
    MovieDTO movie = moviesAPI.getMovie(movieId);

    movie.genres().forEach(
        genreDTO -> userAlgorithmService.improveGenreWeight(genreDTO, user)
    );

    return movie;
  }

  public PagedModel<MovieDTO> findPopularMovies(Pageable pageable) {
    return moviesAPI.getPopularMovies(pageable);
  }
}
