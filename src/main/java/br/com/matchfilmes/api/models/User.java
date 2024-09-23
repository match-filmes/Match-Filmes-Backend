package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String fullName;
  private String username;
  private String email;
  private String password;

  @OneToOne(mappedBy = "user")
  private UserAlgorithm userAlgorithm;
}