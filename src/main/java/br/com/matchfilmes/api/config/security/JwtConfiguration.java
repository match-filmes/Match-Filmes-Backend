package br.com.matchfilmes.api.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    private String secret;
    private long expirationTime;
    private long expirationTimeRememberMe;

}
