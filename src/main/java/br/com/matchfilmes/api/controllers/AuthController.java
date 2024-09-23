package br.com.matchfilmes.api.controllers;

import br.com.matchfilmes.api.dtos.AuthenticationRequestDTO;
import br.com.matchfilmes.api.dtos.AuthenticationResponseDTO;
import br.com.matchfilmes.api.dtos.UserRegisterDTO;
import br.com.matchfilmes.api.infra.JwtUtil;
import br.com.matchfilmes.api.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterDTO registerDTO) {
        if (userService.userExists(registerDTO.getUsername(), registerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Usuário ou senha já cadastrados.");
        }
        userService.register(registerDTO);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AuthenticationResponseDTO("Dados inválidos."));
        }

        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails, authRequest.isRememberMe());
        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
    }
}
