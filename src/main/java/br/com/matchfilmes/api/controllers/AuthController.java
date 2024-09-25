package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.LoginRequestDTO;
import br.com.matchfilmes.api.dtos.AuthenticationResponseDTO;
import br.com.matchfilmes.api.dtos.RegisterRequestDTO;
import br.com.matchfilmes.api.dtos.factories.UserDTOFactory;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.services.AuthenticationService;
import br.com.matchfilmes.api.services.TokenService;
import br.com.matchfilmes.api.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final UserService userService;
  private final AuthenticationService authenticationService;
  private final TokenService tokenService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerDTO) throws ResponseStatusException {
    User user = userService.createUser(registerDTO);
    String token = authenticationService.authenticate(registerDTO.username(), registerDTO.password(), false);

    AuthenticationResponseDTO response = new AuthenticationResponseDTO(
        token,
        UserDTOFactory.create(user)
    );

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
    User user = userService.loadUserByUsername(loginRequest.username());
    String token = authenticationService.authenticate(loginRequest.username(), loginRequest.password(), loginRequest.rememberUser());

    AuthenticationResponseDTO response = new AuthenticationResponseDTO(
        token,
        UserDTOFactory.create(user)
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthenticationResponseDTO> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    User user = tokenService.extractUser(authHeader);
    String token = tokenService.generateToken(user, true);
    
    AuthenticationResponseDTO response = new AuthenticationResponseDTO(
        token,
        UserDTOFactory.create(user)
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
