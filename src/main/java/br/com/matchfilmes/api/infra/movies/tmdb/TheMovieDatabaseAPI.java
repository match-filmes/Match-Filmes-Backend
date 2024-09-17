package br.com.matchfilmes.api.infra.movies.tmdb;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.ImagesDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.TMDBImageResponse;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.TMDBMovieResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
    String url = TMDBUrl.url(path, List.of("append_to_response=images", "include_image_language=pt-BR,null"));

    ResponseEntity<TMDBMovieResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieResponse>(headers), TMDBMovieResponse.class);
    TMDBMovieResponse tmdbMovieResponse = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, tmdbMovieResponse));

    assert tmdbMovieResponse != null;
    MovieDTO movieDTO = new MovieDTO(
        tmdbMovieResponse.id(),
        tmdbMovieResponse.title(),
        tmdbMovieResponse.overview(),
        tmdbMovieResponse.vote_average(),
        tmdbMovieResponse.genres().stream().map(tmdbGenreResponse -> new GenreDTO(tmdbGenreResponse.name(), tmdbGenreResponse.id())).toList(),
        new ImagesDTO(
            tmdbMovieResponse.images().backdrops().stream().map(TMDBImageResponse::file_path).toList(),
            tmdbMovieResponse.images().logos().stream().map(TMDBImageResponse::file_path).toList(),
            tmdbMovieResponse.images().posters().stream().map(TMDBImageResponse::file_path).toList()
        )
    );

    logger.info(String.format("DTO created from response -> %s", movieDTO));

    return movieDTO;
  }

  @Override
  public Set<MovieDTO> getPopularMovies() {
    return Set.of();
  }

  @Override
  public Set<MovieDTO> getRecommendedMovies(Long movieId) {
    return Set.of();
  }

  @Override
  public Set<MovieDTO> getSimilarMovies(Long movieId) {
    return Set.of();
  }

  @Override
  public Set<MovieDTO> getMovies(String query) {
    return Set.of();
  }

}
