package br.com.matchfilmes.api.config;

import br.com.matchfilmes.api.infra.errors.RestTemplateResponseErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    return restTemplate;
  }
}
