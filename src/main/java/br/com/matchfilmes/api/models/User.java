package br.com.matchfilmes.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "_user")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  private String username;
  private String password;

  @OneToOne(mappedBy = "user")
  private UserAlgorithm userAlgorithm;
}
