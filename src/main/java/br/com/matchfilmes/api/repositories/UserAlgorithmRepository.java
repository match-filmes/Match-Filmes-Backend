package br.com.matchfilmes.api.repositories;

import br.com.matchfilmes.api.models.UserAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAlgorithmRepository extends JpaRepository<UserAlgorithm, UUID> {
}
