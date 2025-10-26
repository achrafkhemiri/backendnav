package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "L'email est obligatoire")
    @jakarta.validation.constraints.Email(message = "L'email doit être valide")
    private String mail;

    @jakarta.validation.constraints.NotNull(message = "Le mot de passe est obligatoire")
    @jakarta.validation.constraints.Size(min = 6, max = 100, message = "Le mot de passe doit comporter au moins 6 caractères")
    private String password;
}
