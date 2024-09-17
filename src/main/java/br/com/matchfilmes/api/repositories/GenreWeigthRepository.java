package br.com.matchfilmes.api.repositories;

import br.com.matchfilmes.api.models.GenreWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreWeigthRepository extends JpaRepository<GenreWeight, UUID> {
}
