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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAlgorithmService {
  private final UserAlgorithmRepository userAlgorithmRepository;
  private final GenreWeightRepository genreWeightRepository;
  private static final double IMPROVE_CONSTANT = 0.029879263746;

  public void improveGenreWeight(GenreDTO genreDTO, User user, Double strength) {
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
    double valueToImprove = IMPROVE_CONSTANT;
    if (strength != null) valueToImprove = valueToImprove * strength;
    if (genreWeight.getWeight() + valueToImprove >= 1.0) valueToImprove = 0;

    genreWeight.setWeight(genreWeight.getWeight() + valueToImprove);
    genreWeights.add(genreWeight);

    userAlgorithm.setGenresWeights(genreWeights);
    userAlgorithmRepository.save(userAlgorithm);
  }

  public void decreaseOthersGenresWeights(List<GenreDTO> genresToNotDecrease, User user, Double strength) {
    Set<Long> genreWeightsIds = genresToNotDecrease.stream().map(GenreDTO::id).collect(Collectors.toSet());
    UserAlgorithm userAlgorithm = userAlgorithmRepository.findByUser(user).orElseThrow();
    Set<GenreWeight> genreWeights = userAlgorithm.getGenresWeights();

    genreWeights.stream().filter(
        genre -> {
          if (genre.getGenreId() == null) return false;
          return !genreWeightsIds.contains(genre.getGenreId());
        }
    ).forEach(genre -> {
      GenreWeight genreWeight = genreWeightRepository.findByGenreIdAndUserAlgorithmUser(genre.getGenreId(), user).orElseThrow();
      double valueToDecrease = IMPROVE_CONSTANT / 2;
      if (strength != null) valueToDecrease = valueToDecrease * strength;
      if (genreWeight.getWeight() - valueToDecrease <= 0.0) valueToDecrease = 0;
      genreWeight.setWeight(genreWeight.getWeight() - valueToDecrease);
      genreWeightRepository.save(genreWeight);
    });
  }

  public Set<GenreWeight> getUserTopGenreWeights(User user) throws ResponseStatusException {
    UserAlgorithm userAlgorithm = userAlgorithmRepository.findByUser(user).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return genreWeightRepository.findAllByUserAlgorithmOrderByWeightDesc(userAlgorithm);
  }
}
