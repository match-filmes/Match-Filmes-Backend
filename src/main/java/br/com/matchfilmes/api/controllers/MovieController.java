package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.services.MovieService;
import lombok.AllArgsConstructor;
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
    return new ResponseEntity<MovieDTO>(movieDTO, HttpStatus.OK);
  }
}
