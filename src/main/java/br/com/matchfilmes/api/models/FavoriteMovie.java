package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Table(name = "_favorite_movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteMovie {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private Long movieId;

  @ManyToOne
  @JoinColumn(name = "userAlgorithmId")
  private UserAlgorithm userAlgorithm;
}
