package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.models.GenreWeight;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.models.UserAlgorithm;
import br.com.matchfilmes.api.repositories.UserAlgorithmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserAlgorithmService {
  private final UserAlgorithmRepository userAlgorithmRepository;
  private static final double IMPROVE_CONSTANT = 0.05;

  public void improveGenreWeight(GenreDTO genreDTO, User user) {
    UserAlgorithm userAlgorithm = userAlgorithmRepository.findByUser(user).orElse(
        UserAlgorithm.builder()
            .user(user)
            .build()
    );

    Set<GenreWeight> genreWeights = userAlgorithm.getGenresWeights();


    Optional<GenreWeight> genreWeightOptional = genreWeights.stream().filter(
        genre -> genre.getGenreId().equals(genreDTO.id())
    ).findFirst();

    GenreWeight genreWeight;

    if (genreWeightOptional.isPresent()) {
      genreWeight = genreWeightOptional.get();
      genreWeights.remove(genreWeight);
    } else {
      genreWeight = GenreWeight.builder()
          .genreId(genreDTO.id())
          .weight(0.1)
          .userAlgorithm(userAlgorithm)
          .build();
    }
    genreWeight.setWeight(genreWeight.getWeight() + IMPROVE_CONSTANT);
    genreWeights.add(genreWeight);

    userAlgorithm.setGenresWeights(genreWeights);
    userAlgorithmRepository.save(userAlgorithm);
  }
}
