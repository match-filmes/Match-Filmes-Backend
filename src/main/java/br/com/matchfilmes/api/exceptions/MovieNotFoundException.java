package br.com.matchfilmes.api.exceptions;

import lombok.Getter;

@Getter
public class MovieNotFoundException extends Exception {
  String reason;
}
