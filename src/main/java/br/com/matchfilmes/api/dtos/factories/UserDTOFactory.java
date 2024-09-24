package br.com.matchfilmes.api.dtos.factories;

import br.com.matchfilmes.api.dtos.UserDTO;
import br.com.matchfilmes.api.models.User;

public class UserDTOFactory {
  public static UserDTO create(User user) {
    return new UserDTO(
        user.getId(),
        user.getFullName(),
        user.getUsername(),
        user.getEmail()
    );
  }
}
