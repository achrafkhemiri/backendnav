package com.example.navire.services;

import com.example.navire.model.Projet;
import com.example.navire.model.ProjetClient;
import com.example.navire.model.ProjetDepot;
import com.example.navire.model.Voyage;
import com.example.navire.repository.ProjetClientRepository;
import com.example.navire.repository.ProjetDepotRepository;
import com.example.navire.repository.ProjetRepository;
import com.example.navire.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuantiteService {
    
    private final ProjetRepository projetRepository;
    private final ProjetClientRepository projetClientRepository;
    private final ProjetDepotRepository projetDepotRepository;
    private final VoyageRepository voyageRepository;
    private final NotificationService notificationService;
    
    /**
     * Calcule la quantité totale utilisée dans un projet
     * Quantité utilisée = Somme des quantités autorisées clients + Somme des quantités voyages vers dépôts
     */
    @Transactional(readOnly = true)
    public double calculerQuantiteUtilisee(Long projetId) {
        double quantiteClients = calculerQuantiteClients(projetId);
        double quantiteDepots = calculerQuantiteDepotsAutorises(projetId);
        return quantiteClients + quantiteDepots;
    }
    
    /**
     * Calcule la somme des quantités autorisées pour tous les clients d'un projet
     */
    @Transactional(readOnly = true)
    public double calculerQuantiteClients(Long projetId) {
        List<ProjetClient> projetClients = projetClientRepository.findByProjetId(projetId);
    return projetClients.stream()
        .mapToDouble(this::sommeAutorisation)
        .sum();
    }
    
    /**
     * Calcule la somme des quantités autorisées pour tous les dépôts d'un projet
     */
    @Transactional(readOnly = true)
    public double calculerQuantiteDepotsAutorises(Long projetId) {
        List<ProjetDepot> projetDepots = projetDepotRepository.findByProjetId(projetId);
        return projetDepots.stream()
                .filter(pd -> pd.getQuantiteAutorisee() != null) // Exclure les valeurs null
                .mapToDouble(ProjetDepot::getQuantiteAutorisee)
                .sum();
    }
    
    /**
     * Calcule la somme des quantités des voyages vers les dépôts pour un projet
     */
    @Transactional(readOnly = true)
    public double calculerQuantiteDepots(Long projetId) {
        List<Voyage> voyages = voyageRepository.findByProjetId(projetId);
        return voyages.stream()
                .filter(v -> v.getDepot() != null) // Uniquement les voyages vers dépôts
                .mapToDouble(Voyage::getQuantite)
                .sum();
    }
    
    /**
     * Calcule la quantité restante disponible dans un projet
     */
    @Transactional(readOnly = true)
    public double calculerQuantiteRestante(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        double quantiteUtilisee = calculerQuantiteUtilisee(projetId);
        return projet.getQuantiteTotale() - quantiteUtilisee;
    }
    
    /**
     * Calcule le reste pour un client spécifique
     * Reste = Quantité autorisée - Quantité livrée (somme des voyages)
     */
    @Transactional(readOnly = true)
    public double calculerResteClient(Long projetClientId) {
        ProjetClient projetClient = projetClientRepository.findById(projetClientId)
                .orElseThrow(() -> new RuntimeException("ProjetClient non trouvé"));
        
        List<Voyage> voyages = voyageRepository.findByProjetClientId(projetClientId);
        double quantiteLivree = voyages.stream()
                .mapToDouble(Voyage::getQuantite)
                .sum();
        
        return sommeAutorisation(projetClient) - quantiteLivree;
    }
    
    /**
     * Calcule les statistiques de quantité pour un client dans un projet
     */
    @Transactional(readOnly = true)
    public Map<String, Double> getStatistiquesClient(Long projetClientId) {
        ProjetClient projetClient = projetClientRepository.findById(projetClientId)
                .orElseThrow(() -> new RuntimeException("ProjetClient non trouvé"));
        
        List<Voyage> voyages = voyageRepository.findByProjetClientId(projetClientId);
        double quantiteLivree = voyages.stream()
                .mapToDouble(Voyage::getQuantite)
                .sum();
        
    double quantiteAutorisee = sommeAutorisation(projetClient);
    double reste = quantiteAutorisee - quantiteLivree;
    double pourcentageUtilise = quantiteAutorisee == 0 ? 0 : (quantiteLivree / quantiteAutorisee) * 100;
        
        Map<String, Double> stats = new HashMap<>();
    stats.put("quantiteAutorisee", quantiteAutorisee);
        stats.put("quantiteLivree", quantiteLivree);
        stats.put("reste", reste);
        stats.put("pourcentageUtilise", pourcentageUtilise);
        
        return stats;
    }
    
    /**
     * Calcule les statistiques complètes d'un projet
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistiquesProjet(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        
        double quantiteClients = calculerQuantiteClients(projetId);
        double quantiteDepots = calculerQuantiteDepots(projetId);
        double quantiteUtilisee = quantiteClients + quantiteDepots;
        double quantiteRestante = projet.getQuantiteTotale() - quantiteUtilisee;
        double pourcentageUtilise = (quantiteUtilisee / projet.getQuantiteTotale()) * 100;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("quantiteTotale", projet.getQuantiteTotale());
        stats.put("quantiteClients", quantiteClients);
        stats.put("quantiteDepots", quantiteDepots);
        stats.put("quantiteUtilisee", quantiteUtilisee);
        stats.put("quantiteRestante", quantiteRestante);
        stats.put("pourcentageUtilise", pourcentageUtilise);
        
        return stats;
    }
    
    /**
     * Vérifie si une quantité peut être ajoutée au projet
     */
    @Transactional(readOnly = true)
    public boolean peutAjouterQuantite(Long projetId, double quantite) {
        double quantiteRestante = calculerQuantiteRestante(projetId);
        return quantiteRestante >= quantite;
    }
    
    /**
     * Valide l'ajout d'un client avec une quantité autorisée
     * Génère des notifications si nécessaire
     */
    @Transactional
    public ValidationResult validerAjoutClient(Long projetId, double quantiteAutorisee) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        
        double quantiteRestante = calculerQuantiteRestante(projetId);
        
        ValidationResult result = new ValidationResult();
        result.setValide(quantiteRestante >= quantiteAutorisee);
        result.setQuantiteRestante(quantiteRestante);
        result.setQuantiteDemandee(quantiteAutorisee);
        
        if (!result.isValide()) {
            // Créer une notification d'erreur
            String message = String.format(
                "Impossible d'ajouter le client au projet ID '%d'. " +
                "Quantité demandée: %.2f, Quantité restante: %.2f",
                projetId, quantiteAutorisee, quantiteRestante
            );
            notificationService.creerNotification(
                NotificationService.TypeNotification.DEPASSEMENT_QUANTITE,
                NotificationService.NiveauAlerte.DANGER,
                message,
                "PROJET",
                projetId
            );
            result.setMessage(message);
        } else {
            // Vérifier si on est proche de la limite (>80%)
            double nouvelleQuantiteUtilisee = calculerQuantiteUtilisee(projetId) + quantiteAutorisee;
            double pourcentage = (nouvelleQuantiteUtilisee / projet.getQuantiteTotale()) * 100;
            
            if (pourcentage >= 90) {
                String message = String.format(
                    "⚠️ Attention! Le projet ID '%d' atteint %.2f%% de sa capacité.",
                    projetId, pourcentage
                );
                notificationService.creerNotification(
                    NotificationService.TypeNotification.QUANTITE_PROCHE_LIMITE,
                    NotificationService.NiveauAlerte.DANGER,
                    message,
                    "PROJET",
                    projetId
                );
            } else if (pourcentage >= 80) {
                String message = String.format(
                    "Le projet ID '%d' atteint %.2f%% de sa capacité.",
                    projetId, pourcentage
                );
                notificationService.creerNotification(
                    NotificationService.TypeNotification.QUANTITE_PROCHE_LIMITE,
                    NotificationService.NiveauAlerte.WARNING,
                    message,
                    "PROJET",
                    projetId
                );
            }
            
            result.setMessage("Validation réussie");
        }
        
        return result;
    }
    
    /**
     * Valide l'ajout d'un dépôt avec une quantité autorisée
     * Génère des notifications si nécessaire
     */
    @Transactional
    public ValidationResult validerAjoutDepot(Long projetId, double quantiteAutorisee) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));
        
        double quantiteRestante = calculerQuantiteRestante(projetId);
        
        ValidationResult result = new ValidationResult();
        result.setValide(quantiteRestante >= quantiteAutorisee);
        result.setQuantiteRestante(quantiteRestante);
        result.setQuantiteDemandee(quantiteAutorisee);
        
        if (!result.isValide()) {
            // Créer une notification d'erreur
            String message = String.format(
                "Impossible d'ajouter le dépôt au projet ID '%d'. " +
                "Quantité demandée: %.2f, Quantité restante: %.2f",
                projetId, quantiteAutorisee, quantiteRestante
            );
            notificationService.creerNotification(
                NotificationService.TypeNotification.DEPASSEMENT_QUANTITE,
                NotificationService.NiveauAlerte.DANGER,
                message,
                "PROJET",
                projetId
            );
            result.setMessage(message);
        } else {
            // Vérifier si on est proche de la limite (>80%)
            double nouvelleQuantiteUtilisee = calculerQuantiteUtilisee(projetId) + quantiteAutorisee;
            double pourcentage = (nouvelleQuantiteUtilisee / projet.getQuantiteTotale()) * 100;
            
            if (pourcentage >= 90) {
                String message = String.format(
                    "⚠️ Attention! Le projet ID '%d' atteint %.2f%% de sa capacité.",
                    projetId, pourcentage
                );
                notificationService.creerNotification(
                    NotificationService.TypeNotification.QUANTITE_PROCHE_LIMITE,
                    NotificationService.NiveauAlerte.DANGER,
                    message,
                    "PROJET",
                    projetId
                );
            } else if (pourcentage >= 80) {
                String message = String.format(
                    "Le projet ID '%d' atteint %.2f%% de sa capacité.",
                    projetId, pourcentage
                );
                notificationService.creerNotification(
                    NotificationService.TypeNotification.QUANTITE_PROCHE_LIMITE,
                    NotificationService.NiveauAlerte.WARNING,
                    message,
                    "PROJET",
                    projetId
                );
            }
            
            result.setMessage("Validation réussie");
        }
        
        return result;
    }
    
    /**
     * Valide l'ajout d'un voyage
     */
    @Transactional
    public ValidationResult validerAjoutVoyage(Long projetClientId, double quantite) {
        ProjetClient projetClient = projetClientRepository.findById(projetClientId)
                .orElseThrow(() -> new RuntimeException("ProjetClient non trouvé"));
        
        double resteClient = calculerResteClient(projetClientId);
        
        ValidationResult result = new ValidationResult();
        result.setValide(resteClient >= quantite);
        result.setQuantiteRestante(resteClient);
        result.setQuantiteDemandee(quantite);
        
        if (!result.isValide()) {
            String message = String.format(
                "Impossible d'ajouter le voyage. Quantité demandée: %.2f, Reste client: %.2f",
                quantite, resteClient
            );
            notificationService.creerNotification(
                NotificationService.TypeNotification.VALIDATION_VOYAGE,
                NotificationService.NiveauAlerte.DANGER,
                message,
                "CLIENT",
                projetClient.getClient().getId()
            );
            result.setMessage(message);
        } else {
            result.setMessage("Validation réussie");
        }
        
        return result;
    }

    /**
     * Somme des quantités définies dans la collection d'autorisations d'un ProjetClient.
     */
    private double sommeAutorisation(ProjetClient pc) {
        if (pc == null || pc.getAutorisation() == null) return 0.0;
        return pc.getAutorisation().stream()
                .filter(Objects::nonNull)
                .mapToDouble(a -> a.getQuantite() == null ? 0.0 : a.getQuantite())
                .sum();
    }
    
    /**
     * Classe pour le résultat de validation
     */
    public static class ValidationResult {
        private boolean valide;
        private String message;
        private double quantiteRestante;
        private double quantiteDemandee;
        
        public boolean isValide() { return valide; }
        public void setValide(boolean valide) { this.valide = valide; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public double getQuantiteRestante() { return quantiteRestante; }
        public void setQuantiteRestante(double quantiteRestante) { this.quantiteRestante = quantiteRestante; }
        
        public double getQuantiteDemandee() { return quantiteDemandee; }
        public void setQuantiteDemandee(double quantiteDemandee) { this.quantiteDemandee = quantiteDemandee; }
    }
}
