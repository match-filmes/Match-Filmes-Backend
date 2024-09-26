package br.com.matchfilmes.api.controllers.advicers;

import br.com.matchfilmes.api.exceptions.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MovieNotFoundAdvicer {
  @ExceptionHandler(MovieNotFoundException.class)
  public ResponseEntity<?> handleResponseStatusException(MovieNotFoundException e) {
    return e.getReason() == null ?
        ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getReason());
  }
}
