package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public String authenticate(String username, String password, boolean rememberUser) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(username, password);
    var auth = this.authenticationManager.authenticate(usernamePassword);
    return tokenService.generateToken((User) auth.getPrincipal(), rememberUser);
  }
}
