package br.com.matchfilmes.api.infra.movies.tmdb;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.TMDBMovieResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@AllArgsConstructor
@Component
public class TheMovieDatabaseAPI implements MoviesAPI {
  private final RestTemplate restTemplate;
  private final HttpHeaders headers;
  private final Logger logger;

  @Override
  public MovieDTO getMovie(Long movieId) {
    String path = "/movie/" + movieId;
    String url = TMDBUrl.url(path);

    ResponseEntity<TMDBMovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieResponse>(headers), TMDBMovieResponse.class);
    TMDBMovieResponse tmdbMovieResponse = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, tmdbMovieResponse));

    assert tmdbMovieResponse != null;
    MovieDTO movieDTO = new MovieDTO(
        tmdbMovieResponse.title(),
        tmdbMovieResponse.overview(),
        tmdbMovieResponse.poster_path(),
        tmdbMovieResponse.genres().stream().map(tmdbGenreResponse -> new GenreDTO(tmdbGenreResponse.name(), tmdbGenreResponse.id())).toList(),
        tmdbMovieResponse.id()
    );

    logger.info(String.format("DTO created from response -> %s", movieDTO));

    return movieDTO;
  }

  @Override
  public Set<MovieDTO> getMovies() {
    return Set.of();
  }

  @Override
  public Set<MovieDTO> getMovies(String query) {
    return Set.of();
  }

  @Override
  public GenreDTO getGenre(Long genreId) {
    return null;
  }

  @Override
  public Set<GenreDTO> getGenres() {
    return Set.of();
  }

  @Override
  public Set<GenreDTO> getGenres(String query) {
    return Set.of();
  }
}
