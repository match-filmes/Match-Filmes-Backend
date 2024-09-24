package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "_genre_weight")
@Data
@Builder
public class GenreWeight {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private Long genreId;
  private Double weight;

  @ManyToOne
  @JoinColumn(name = "userAlgorithmId")
  private UserAlgorithm userAlgorithm;
}
