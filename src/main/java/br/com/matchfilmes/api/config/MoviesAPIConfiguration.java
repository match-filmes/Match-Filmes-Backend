package br.com.matchfilmes.api.config;

import br.com.matchfilmes.api.infra.movies.MoviesAPI;
import br.com.matchfilmes.api.infra.movies.tmdb.TheMovieDatabaseAPI;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class MoviesAPIConfiguration {
  private final RestTemplate restTemplate;
  private final HttpHeaders httpHeaders;
  private final Logger logger;

  @Bean
  public MoviesAPI moviesAPI() {
    return new TheMovieDatabaseAPI(restTemplate, httpHeaders, logger);
  }
}
