package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Table(name = "_user_algorithm")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAlgorithm {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(mappedBy = "userAlgorithm", cascade = CascadeType.ALL)
  private Set<FavoriteMovie> favoriteMovies;

  @OneToOne
  @JoinColumn(name = "userId")
  private User user;

  @OneToMany(mappedBy = "userAlgorithm", cascade = CascadeType.ALL)
  private Set<GenreWeight> genresWeights;
}
