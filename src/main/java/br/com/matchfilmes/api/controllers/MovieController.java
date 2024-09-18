package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  @GetMapping("/{id}")
  public ResponseEntity<MovieDTO> findMovieById(@PathVariable Long id) {
    MovieDTO movieDTO = movieService.findById(id);
    return new ResponseEntity<>(movieDTO, HttpStatus.OK);
  }

  @GetMapping("/popular")
  public ResponseEntity<PagedModel<MovieDTO>> findMovieById(Pageable pageable) {
    PagedModel<MovieDTO> page = movieService.findPopularMovies(pageable);
    return new ResponseEntity<>(page, HttpStatus.OK);
  }
}
