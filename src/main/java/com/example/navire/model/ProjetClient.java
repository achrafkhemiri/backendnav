
package com.example.navire.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "projet_client")
public class ProjetClient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @NotNull(message = "Projet is required")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull(message = "Client is required")
    private Client client;

    /**
     * Nouvelle structure : une collection d'autorisations (code -> quantité)
     * Stockée en tant qu'ElementCollection dans une table jointe `projet_client_autorisations`.
     */
    @Valid
    @ElementCollection
    @CollectionTable(name = "projet_client_autorisations", joinColumns = @JoinColumn(name = "projet_client_id"))
    private Set<Autorisation> autorisation;

    /**
     * Compatibility accessor: retourne la somme des quantités autorisées.
     * Conserve l'API précédente `getQuantiteAutorisee()` utilisée par d'autres composants.
     */
    @Transient
    public Double getQuantiteAutorisee() {
        if (this.autorisation == null || this.autorisation.isEmpty()) return null;
        return this.autorisation.stream()
                .filter(a -> a != null && a.getQuantite() != null)
                .mapToDouble(Autorisation::getQuantite)
                .sum();
    }

    /**
     * Compatibility mutator: permet d'assigner une quantité totale comme avant.
     * Cela remplace les autorisations existantes par une seule entrée avec le code "DEFAULT".
     */
    @Transient
    public void setQuantiteAutorisee(Double quantite) {
        if (quantite == null) {
            this.autorisation = null;
        } else {
            this.autorisation = java.util.Set.of(new Autorisation("DEFAULT", quantite));
        }
    }
}
