package br.com.matchfilmes.api.infra.movies.tmdb;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@AllArgsConstructor
@Component
public class TheMovieDatabaseAPI implements MoviesAPI {
  private final RestTemplate restTemplate;
  private final String url = "https://api.themoviedb.org/3";

  @Value("${env.TMDB.api_key}")
  private final String API_KEY;

  @Override
  public MovieDTO getMovie(Long movieId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(API_KEY);

    String path = url + "/movie/" + movieId;
    System.out.println("api Key: " + API_KEY);
    System.out.println(headers);

    ResponseEntity<TMDBMovieResponse> response = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<TMDBMovieResponse>(headers), TMDBMovieResponse.class);
    TMDBMovieResponse tmdbMovieResponse = response.getBody();

    MovieDTO movieDTO = new MovieDTO(
        tmdbMovieResponse.title(),
        tmdbMovieResponse.overview(),
        null,
        null,
        null
    );

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
