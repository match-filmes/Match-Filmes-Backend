package br.com.matchfilmes.api.infra;

import br.com.matchfilmes.api.config.security.JwtConfiguration;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mysecretkey";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas
    private final long EXPIRATION_TIME_REMEMBER_ME = 1000 * 60 * 60 * 24 * 7; // 7 dias
    private final String AMERICA_SAO_PAULO_OFFSET = "-03:00";

    @Lazy
    private final JwtConfiguration jwtConfig;

    @Autowired
    public JwtUtil(JwtConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserDetails userDetails, boolean rememberMe) {
        long expirationTime = rememberMe ? jwtConfig.getExpirationTimeRememberMe() : jwtConfig.getExpirationTime();
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer("daily-manage")
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
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

    public String extractSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer("super-promo-zum")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String extractRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer("super-promo-zum")
                    .build()
                    .verify(token)
                    .getClaim("roles").asString();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String extractJWTToken(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }

    private Instant generateExpirationDate() {
        int TOKEN_EXPIRATION_IN_HOURS = 12;
        return LocalDateTime.now().plusHours(TOKEN_EXPIRATION_IN_HOURS)
                .toInstant(ZoneOffset.of(AMERICA_SAO_PAULO_OFFSET));
    }
}