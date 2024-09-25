package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.RegisterRequestDTO;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User createUser(RegisterRequestDTO request) throws ResponseStatusException {
    assert userNotRegistered(request.username(), request.email()) : new ResponseStatusException(HttpStatus.CONFLICT);

    String encryptedPassword = passwordEncoder.encode(request.password());
    User user = User.builder()
        .fullName(request.fullName())
        .username(request.username())
        .email(request.email())
        .password(encryptedPassword)
        .build();

    return userRepository.save(user);
  }

  private boolean userNotRegistered(String username, String email) {
    return userRepository.findByUsernameOrEmail(username, email).isEmpty();
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Error: User not found"));
  }
}
