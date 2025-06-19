package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository pour l'entité Adresse.
 * Fournit les opérations CRUD de base pour les adresses.
 */
@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    List<Adresse> findByVille(String ville);
} 