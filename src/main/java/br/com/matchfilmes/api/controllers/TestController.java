package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.infra.movies.tmdb.TheMovieDatabaseAPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {
  private final TheMovieDatabaseAPI theMovieDatabaseAPI;

  @GetMapping("test/{id}")
  public MovieDTO test(@PathVariable Long id) {
    return theMovieDatabaseAPI.getMovie(id);
  }
}
