package br.com.matchfilmes.api.repositories;

import br.com.matchfilmes.api.models.FavoriteMovie;
import br.com.matchfilmes.api.models.UserAlgorithm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, UUID> {
  Optional<FavoriteMovie> findByMovieIdAndUserAlgorithm(Long movieId, UserAlgorithm userAlgorithm);
  Page<FavoriteMovie> findAllByUserAlgorithm(UserAlgorithm userAlgorithm, Pageable pageable);
  Long countByUserAlgorithm(UserAlgorithm userAlgorithm);
}
