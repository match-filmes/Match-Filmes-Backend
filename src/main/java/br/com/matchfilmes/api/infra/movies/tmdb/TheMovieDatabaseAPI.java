package br.com.matchfilmes.api.infra.movies.tmdb;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.ImagesDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.TMDBImageDTO;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.TMDBMovieDTO;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.MovieListDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    ResponseEntity<TMDBMovieDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieDTO>(headers), TMDBMovieDTO.class);
    TMDBMovieDTO tmdbMovieDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, tmdbMovieDTO));

    assert tmdbMovieDTO != null;
    MovieDTO movieDTO = new MovieDTO(
        tmdbMovieDTO.id(),
        tmdbMovieDTO.title(),
        tmdbMovieDTO.overview(),
        tmdbMovieDTO.vote_average(),
        tmdbMovieDTO.genres().stream().map(tmdbGenreResponse -> new GenreDTO(tmdbGenreResponse.name(), tmdbGenreResponse.id())).toList(),
        new ImagesDTO(
            tmdbMovieDTO.images().backdrops().stream().map(TMDBImageDTO::file_path).toList(),
            tmdbMovieDTO.images().logos().stream().map(TMDBImageDTO::file_path).toList(),
            tmdbMovieDTO.images().posters().stream().map(TMDBImageDTO::file_path).toList()
        ),
        tmdbMovieDTO.poster_path()
    );

    logger.info(String.format("DTO created from response -> %s", movieDTO));

    return movieDTO;
  }

  @Override
  public PagedModel<MovieDTO> getPopularMovies(Pageable pageable) {
    String path = "/movie/popular";
    String url = TMDBUrl.url(path, List.of("page=" + (pageable.getPageNumber()+1)));
    ResponseEntity<MovieListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<MovieListDTO>(headers), MovieListDTO.class);
    MovieListDTO movieListDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, movieListDTO));

    assert movieListDTO != null;
    List<MovieDTO> movies = movieListDTO.results().stream().map(
        tmdbMovieDTO -> new MovieDTO(
            tmdbMovieDTO.id(),
            tmdbMovieDTO.title(),
            tmdbMovieDTO.overview(),
            tmdbMovieDTO.vote_average(),
            null,
            null,
            tmdbMovieDTO.poster_path()
        )
    ).toList();
    PagedModel<MovieDTO> pagedModel = new PagedModel<>(new PageImpl<>(movies, pageable, movieListDTO.total_results()));

    logger.info(String.format("DTO created from response -> %s", movies));

    return pagedModel;
  }

  @Override
  public Set<MovieDTO> getRecommendedMoviesByGenres(Pageable pageable, Long[] genresIds) {
    String path = "/discover/movie";
    String genresIdToParam = Arrays.stream(genresIds).map(id -> id.toString() + "|").collect(Collectors.joining());
    String url = TMDBUrl.url(path, List.of("page=" + (pageable.getPageNumber()+1), "with_genres=" + genresIdToParam));
    System.out.println(url);
    ResponseEntity<MovieListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<MovieListDTO>(headers), MovieListDTO.class);
    MovieListDTO movieListDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, movieListDTO));

    assert movieListDTO != null;
    Set<MovieDTO> movies = movieListDTO.results().stream().map(
        tmdbMovieDTO -> new MovieDTO(
            tmdbMovieDTO.id(),
            tmdbMovieDTO.title(),
            tmdbMovieDTO.overview(),
            tmdbMovieDTO.vote_average(),
            null,
            null,
            tmdbMovieDTO.poster_path()
        )
    ).collect(Collectors.toSet());

    logger.info(String.format("DTO created from response -> %s", movies));

    return movies;
  }

  @Override
  public PagedModel<MovieDTO> getRecommendedMovies(Pageable pageable, Long movieId) {
    return null;
  }

  @Override
  public PagedModel<MovieDTO> getSimilarMovies(Pageable pageable, Long movieId) {
    return null;
  }

  @Override
  public PagedModel<MovieDTO> getMovies(Pageable pageable, String query) {
    return null;
  }

}
