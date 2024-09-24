package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.infra.security.JwtProperties;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class TokenService {
  private final UserRepository userRepository;
  private final String AMERICA_SAO_PAULO_OFFSET = "-03:00";
  private final JwtProperties jwtProperties;

  protected String generateToken(UserDetails userDetails, boolean rememberUser) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
      return JWT.create()
          .withIssuer("match-filmes")
          .withSubject(userDetails.getUsername())
          .withExpiresAt(generateExpirationDate(rememberUser))
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error while generating token", exception);
    }
  }

  public boolean isTokenNotExpired(String token) {
    DecodedJWT decodedJWT = JWT.decode(token);
    Instant expireDate = decodedJWT.getExpiresAtAsInstant();
    return LocalDateTime.now().toInstant(ZoneOffset.of(AMERICA_SAO_PAULO_OFFSET)).isBefore(expireDate);
  }

  public User extractUser(String bearerToken) {
    String token = extractJWTToken(bearerToken);
    String subject = extractSubject(token);
    return userRepository.findByUsername(subject).orElseThrow();
  }

  private String extractSubject(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
      return JWT.require(algorithm)
          .withIssuer("match-filmes")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      return "";
    }
  }

  private String extractJWTToken(String bearerToken) {
    return bearerToken.replace("Bearer ", "");
  }

  protected Instant generateExpirationDate(boolean rememberUser) {
    final long EXPIRATION_TIME_IN_HOURS = 10;
    final long EXPIRATION_TIME_IN_DAYS = 7;

    return rememberUser ?
        LocalDateTime.now().plusDays(EXPIRATION_TIME_IN_DAYS)
            .toInstant(ZoneOffset.of(AMERICA_SAO_PAULO_OFFSET)) :
        LocalDateTime.now().plusHours(EXPIRATION_TIME_IN_HOURS)
            .toInstant(ZoneOffset.of(AMERICA_SAO_PAULO_OFFSET));
  }
}
