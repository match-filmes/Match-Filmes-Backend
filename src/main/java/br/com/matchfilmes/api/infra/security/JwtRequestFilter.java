package br.com.matchfilmes.api.infra.security;

import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.repositories.UserRepository;
import br.com.matchfilmes.api.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var token = this.recoverToken(request);
    if (token != null) {
      String subject = tokenService.extractSubject(token);
      User user = userRepository.findByUsername(subject).orElseThrow();
      Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

      assert tokenService.isTokenNotExpired(token) : "Token expirado, faça login novamente";
      var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null) {
      return null;
    }
    return authHeader.replace("Bearer ", "");
  }
}