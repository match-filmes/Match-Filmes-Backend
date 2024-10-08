package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.services.MovieService;
import br.com.matchfilmes.api.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;
  private final TokenService tokenService;

  @GetMapping("/{id}")
  public ResponseEntity<MovieDTO> findMovieById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws MovieNotFoundException {
    User user = tokenService.extractUser(authHeader);
    MovieDTO movieDTO = movieService.findById(id, user);
    return new ResponseEntity<>(movieDTO, HttpStatus.OK);
  }

  @GetMapping("/popular")
  public ResponseEntity<PagedModel<MovieDTO>> findPopularMovies(Pageable pageable) {
    PagedModel<MovieDTO> page = movieService.findPopularMovies(pageable);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @GetMapping("/recommended")
  public ResponseEntity<PagedModel<MovieDTO>> findRecommendedMovies(Pageable pageable, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    User user = tokenService.extractUser(authHeader);
    PagedModel<MovieDTO> page = movieService.findRecommendedMovies(pageable, user);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  @GetMapping("/similar/{id}")
  public ResponseEntity<PagedModel<MovieDTO>> findSimilarMovies(Pageable pageable, @PathVariable Long id) {
    PagedModel<MovieDTO> page = movieService.findSimilarMovies(pageable, id);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }
}
