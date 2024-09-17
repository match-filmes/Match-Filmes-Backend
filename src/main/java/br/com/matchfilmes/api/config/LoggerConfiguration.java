package br.com.matchfilmes.api.config;

import br.com.matchfilmes.api.ApiApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {
  @Bean
  public Logger logger() {
    return LoggerFactory.getLogger(ApiApplication.class);
  }
}
