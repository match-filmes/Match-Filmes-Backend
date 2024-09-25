package br.com.matchfilmes.api.services;

import br.com.matchfilmes.api.dtos.GenreDTO;
import br.com.matchfilmes.api.models.GenreWeight;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.models.UserAlgorithm;
import br.com.matchfilmes.api.repositories.GenreWeightRepository;
import br.com.matchfilmes.api.repositories.UserAlgorithmRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserAlgorithmService {
  private final UserAlgorithmRepository userAlgorithmRepository;
  private final GenreWeightRepository genreWeightRepository;
  private static final double IMPROVE_CONSTANT = 0.029879263746;

  public void improveGenreWeight(GenreDTO genreDTO, User user) {
    UserAlgorithm userAlgorithm = userAlgorithmRepository.findByUser(user).orElse(
        UserAlgorithm.builder()
            .user(user)
            .build()
    );
    Set<GenreWeight> genreWeights = userAlgorithm.getGenresWeights();
    if (genreWeights == null) genreWeights = new HashSet<>();

    Optional<GenreWeight> genreWeightOptional = genreWeights.stream().filter(
        genre -> {
          if (genre.getGenreId() == null) return false;
          return genre.getGenreId().equals(genreDTO.id());
        }
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
    double valeuToImprove = IMPROVE_CONSTANT;
    if (genreWeight.getWeight() + valeuToImprove >= 1.0) valeuToImprove = 0;

    genreWeight.setWeight(genreWeight.getWeight() + valeuToImprove);
    genreWeights.add(genreWeight);

    userAlgorithm.setGenresWeights(genreWeights);
    userAlgorithmRepository.save(userAlgorithm);

    genreWeights.stream().filter(
        genre -> {
          if (genre.getGenreId() == null) return false;
          return !genre.getGenreId().equals(genreDTO.id());
        }
    ).forEach(genre -> decreaseGenreWeight(genre.getGenreId(), user));
  }

  private void decreaseGenreWeight(Long genreId, User user) {
    GenreWeight genreWeight = genreWeightRepository.findByGenreIdAndUserAlgorithmUser(genreId, user).orElseThrow();
    double valeuToDecrease = IMPROVE_CONSTANT/2;
    if (genreWeight.getWeight() - valeuToDecrease <= 0.0) valeuToDecrease = 0;
    genreWeight.setWeight(genreWeight.getWeight() - valeuToDecrease);
    genreWeightRepository.save(genreWeight);
  }

  public Set<GenreWeight> getUserTopGenreWeights(User user) throws ResponseStatusException {
    UserAlgorithm userAlgorithm = userAlgorithmRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return genreWeightRepository.findAllByUserAlgorithmOrderByWeightDesc(userAlgorithm);
  }
}
