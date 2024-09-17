package br.com.matchfilmes.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringConfiguration {
  @Bean
  public String string() {
    return new String();
  }
}
