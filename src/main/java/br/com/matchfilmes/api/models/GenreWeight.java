package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "_genre_weight")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreWeight {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(unique = true)
  private Long genreId;
  private Double weight;

  @ManyToOne
  @JoinColumn(name = "userAlgorithmId")
  private UserAlgorithm userAlgorithm;
}
