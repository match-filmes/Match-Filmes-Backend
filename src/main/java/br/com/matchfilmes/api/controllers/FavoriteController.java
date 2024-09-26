package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.MovieDTO;
import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.services.FavoriteService;
import br.com.matchfilmes.api.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/movies/favorite")
public class FavoriteController {
  private final FavoriteService favoriteService;
  private final TokenService tokenService;

  @PostMapping("/{id}")
  public ResponseEntity<MovieDTO> favoriteMovie(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws MovieNotFoundException {
    User user = tokenService.extractUser(authHeader);
    MovieDTO movie = favoriteService.favoriteMovie(id, user);

    return new ResponseEntity<>(movie, HttpStatus.OK);
  }
}
