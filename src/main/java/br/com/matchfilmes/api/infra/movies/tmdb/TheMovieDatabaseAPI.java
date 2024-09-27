package br.com.matchfilmes.api.infra.movies.tmdb;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.dtos.ImagesDTO;
import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.infra.movies.tmdb.dto.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
  public MovieDTO getMovie(Long movieId) throws MovieNotFoundException {
    String path = "/movie/" + movieId;
    String url = TMDBUrl.url(path, List.of("append_to_response=images", "include_image_language=pt-BR,null"));
    ResponseEntity<TMDBMovieDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieDTO>(headers), TMDBMovieDTO.class);

    if (response.getStatusCode() == HttpStatus.NOT_FOUND) throw new MovieNotFoundException();

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
    ResponseEntity<TMDBMovieListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieListDTO>(headers), TMDBMovieListDTO.class);
    TMDBMovieListDTO TMDBMovieListDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, TMDBMovieListDTO));

    assert TMDBMovieListDTO != null;
    Set<TMDBGenreDTO> genres = getGenres();
    List<MovieDTO> movies = TMDBMovieListDTO.results().stream().map(
        tmdbMovieDTO -> new MovieDTO(
            tmdbMovieDTO.id(),
            tmdbMovieDTO.title(),
            tmdbMovieDTO.overview(),
            tmdbMovieDTO.vote_average(),
            tmdbMovieDTO.genre_ids().stream().map(id -> this.extractGenreFromGenreSet(id, genres)).toList(),
            null,
            tmdbMovieDTO.poster_path()
        )
    ).toList();
    PagedModel<MovieDTO> pagedModel = new PagedModel<>(new PageImpl<>(movies, pageable, TMDBMovieListDTO.total_results()));

    logger.info(String.format("DTO created from response -> %s", movies));

    return pagedModel;
  }

  @Override
  public Set<MovieDTO> getRecommendedMoviesByGenres(Pageable pageable, Long[] genresIds) {
    String path = "/discover/movie";
    String genresIdToParam = Arrays.stream(genresIds).map(id -> id.toString() + "|").collect(Collectors.joining());
    String url = TMDBUrl.url(path, List.of("page=" + (pageable.getPageNumber()+1), "with_genres=" + genresIdToParam));
    ResponseEntity<TMDBMovieListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieListDTO>(headers), TMDBMovieListDTO.class);
    TMDBMovieListDTO TMDBMovieListDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, TMDBMovieListDTO));

    assert TMDBMovieListDTO != null;
    Set<TMDBGenreDTO> genres = getGenres();
    Set<MovieDTO> movies = TMDBMovieListDTO.results().stream().map(
        tmdbMovieDTO -> new MovieDTO(
            tmdbMovieDTO.id(),
            tmdbMovieDTO.title(),
            tmdbMovieDTO.overview(),
            tmdbMovieDTO.vote_average(),
            tmdbMovieDTO.genre_ids().stream().map(id -> this.extractGenreFromGenreSet(id, genres)).toList(),
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
    String path = "/movie/" + movieId + "/similar";
    String url = TMDBUrl.url(path, List.of("page=" + (pageable.getPageNumber()+1)));
    ResponseEntity<TMDBMovieListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBMovieListDTO>(headers), TMDBMovieListDTO.class);
    TMDBMovieListDTO TMDBMovieListDTO = response.getBody();

    logger.info(String.format("Response from %s -> %s", url, TMDBMovieListDTO));

    assert TMDBMovieListDTO != null;
    Set<TMDBGenreDTO> genres = getGenres();
    List<MovieDTO> movies = TMDBMovieListDTO.results().stream().map(
        tmdbMovieDTO -> new MovieDTO(
            tmdbMovieDTO.id(),
            tmdbMovieDTO.title(),
            tmdbMovieDTO.overview(),
            tmdbMovieDTO.vote_average(),
            tmdbMovieDTO.genre_ids().stream().map(id -> this.extractGenreFromGenreSet(id, genres)).toList(),
            null,
            tmdbMovieDTO.poster_path()
        )
    ).toList();
    PagedModel<MovieDTO> pagedModel = new PagedModel<>(new PageImpl<>(movies, pageable, TMDBMovieListDTO.total_results()));

    logger.info(String.format("DTO created from response -> %s", movies));

    return pagedModel;
  }

  @Override
  public PagedModel<MovieDTO> getMovies(Pageable pageable, String query) {
    return null;
  }

  @Override
  public GenreDTO getGenre(Long id) {
    String path = "/genre/movie/list";
    String url = TMDBUrl.url(path);
    ResponseEntity<TMDBGenreListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBGenreListDTO>(headers), TMDBGenreListDTO.class);
    assert response.getBody() != null;
    Set<TMDBGenreDTO> genres = response.getBody().genres();

    TMDBGenreDTO genre = genres.stream().filter(g -> g.id().equals(id)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new GenreDTO(genre.name(), genre.id());
  }

  private Set<TMDBGenreDTO> getGenres() {
    String path = "/genre/movie/list";
    String url = TMDBUrl.url(path);
    ResponseEntity<TMDBGenreListDTO> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<TMDBGenreListDTO>(headers), TMDBGenreListDTO.class);
    assert response.getBody() != null;
    return response.getBody().genres();
  }

  private GenreDTO extractGenreFromGenreSet(Long genreId, Set<TMDBGenreDTO> genreSet) {
    TMDBGenreDTO genre = genreSet.stream().filter(g -> g.id().equals(genreId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new GenreDTO(genre.name(), genre.id());
  }

}
