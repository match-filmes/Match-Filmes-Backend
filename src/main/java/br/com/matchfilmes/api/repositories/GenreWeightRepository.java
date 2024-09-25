package br.com.matchfilmes.api.repositories;

import br.com.matchfilmes.api.models.GenreWeight;
import br.com.matchfilmes.api.models.User;
import br.com.matchfilmes.api.models.UserAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GenreWeightRepository extends JpaRepository<GenreWeight, UUID> {
  Set<GenreWeight> findAllByUserAlgorithmOrderByWeightDesc(UserAlgorithm userAlgorithm);
  Optional<GenreWeight> findByGenreIdAndUserAlgorithmUser(Long genreId, User user);
}
