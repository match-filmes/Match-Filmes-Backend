package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Table(name = "_user_algorithm")
@Data
public class UserAlgorithm {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;


  @OneToOne
  @JoinColumn(name = "userId")
  private User user;

  @OneToMany(mappedBy = "userAlgorithm")
  private Set<GenreWeight> genresWeights;
}
