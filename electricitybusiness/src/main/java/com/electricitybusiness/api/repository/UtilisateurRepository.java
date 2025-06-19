package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Utilisateur.
 * Fournit les opérations CRUD et quelques méthodes de recherche spécifiques.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    Optional<Utilisateur> findByPseudo(String pseudo);
    
    Optional<Utilisateur> findByAdresseMail(String adresseMail);
    
    boolean existsByPseudo(String pseudo);
    
    boolean existsByAdresseMail(String adresseMail);

    List<Utilisateur> findByRole(RoleUtilisateur role);

    List<Utilisateur> findByLieu(Lieu lieu);
} 