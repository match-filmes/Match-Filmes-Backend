package br.com.matchfilmes.api.controllers.advicers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ResponseStatusAdvicer {
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
    return e.getReason() == null ?
        ResponseEntity.status(e.getStatusCode()).build() :
        ResponseEntity.status(e.getStatusCode()).body(e.getReason());
  }
}
