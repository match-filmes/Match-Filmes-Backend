package br.com.matchfilmes.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterDTO {
    @NotBlank(message = "O nome completo é obrigatório.")
    private String fullName;

    @NotBlank(message = "O usuário é obrigatório.")
    private String username;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail é inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;

    @NotBlank(message = "A confirmação de senha é obrigatória.")
    private String confirmPassword;

    public boolean isPasswordConfirmed() {
        return this.password.equals(this.confirmPassword);
    }
}