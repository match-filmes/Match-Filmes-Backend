package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.UserRegisterDTO;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.repositories.UserRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepositoryRepository userRepository;

    @Lazy
    private final PasswordEncoder passwordEncoder;

    public void register(UserRegisterDTO registerDTO) {
        if (!registerDTO.isPasswordConfirmed()) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        User newUser = new User(
                null,
                registerDTO.getFullName(),
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()),
                null
        );

        userRepository.save(newUser);
    }

    public boolean userExists(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}
