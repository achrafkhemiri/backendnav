package com.example.navire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true)
    private String mail;

    @NotNull(message = "Le mot de passe est obligatoire")
    @Size(min = 6, max = 100, message = "Le mot de passe doit comporter au moins 6 caractères")
    @Column(nullable = false)
    private String password;
}
