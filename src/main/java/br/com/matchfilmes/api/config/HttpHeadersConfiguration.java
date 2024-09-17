package br.com.matchfilmes.api.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
@AllArgsConstructor
public class HttpHeadersConfiguration {
  @Value("${env.TMDB.api_key}")
  private final String API_KEY;

  @Bean
  public HttpHeaders httpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(API_KEY);
    return headers;
  }
}
