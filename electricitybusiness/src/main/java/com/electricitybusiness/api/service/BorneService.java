package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.repository.BorneRepository;
import com.electricitybusiness.api.util.BorneUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.model.Adresse;
import com.electricitybusiness.api.model.Reservation;
import com.electricitybusiness.api.repository.LieuRepository;
import com.electricitybusiness.api.repository.AdresseRepository;
import com.electricitybusiness.api.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Service pour la gestion des bornes électriques.
 * Contient la logique métier pour les opérations CRUD sur les bornes.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BorneService {

    private final BorneRepository borneRepository;
    private final LieuRepository lieuRepository;
    private final AdresseRepository adresseRepository;
    private final ReservationRepository reservationRepository;

    /**
     * Récupère toutes les bornes.
     */
    @Transactional(readOnly = true)
    public List<Borne> findAll() {
        return borneRepository.findAll();
    }

    /**
     * Récupère une borne par son ID.
     */
    @Transactional(readOnly = true)
    public Optional<Borne> findById(Long id) {
        return borneRepository.findById(id);
    }

    /**
     * Crée une nouvelle borne.
     */
    public Borne save(Borne borne) {
        return borneRepository.save(borne);
    }

    /**
     * Met à jour une borne existante.
     */
    public Borne update(Long id, Borne borne) {
        borne.setNumBorne(id);
        return borneRepository.save(borne);
    }

    /**
     * Supprime une borne.
     */
    public void deleteById(Long id) {
        borneRepository.deleteById(id);
    }

    /**
     * Vérifie si une borne existe.
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return borneRepository.existsById(id);
    }

    /**
     * Récupère les bornes à proximité d'une position donnée.
     */
    @Transactional(readOnly = true)
    public List<Borne> findNearbyBornes(double longitude, double latitude, double rayon) {
        List<Borne> allBornes = borneRepository.findAll();
        return BorneUtils.getNearbyBornes(allBornes, longitude, latitude, rayon);
    }

    /**
     * Récupère les bornes disponibles dans une ville à une date donnée (créneau).
     */
    @Transactional(readOnly = true)
    public List<Borne> findAvailableBornesInCityAtDate(String ville, LocalDateTime dateDebut, LocalDateTime dateFin) {
        List<Adresse> adresses = adresseRepository.findByVille(ville);
        List<Borne> bornesDisponibles = new ArrayList<>();
        for (Adresse adresse : adresses) {
            Lieu lieu = adresse.getLieu();
            if (lieu == null) continue;
            List<Borne> bornes = borneRepository.findByLieu(lieu);
            for (Borne borne : bornes) {
                boolean isDisponible = true;
                List<Reservation> reservations = borne.getReservations();
                if (reservations != null) {
                    for (Reservation reservation : reservations) {
                        if (reservation.getDateDebut().isBefore(dateFin) && reservation.getDateFin().isAfter(dateDebut)) {
                            isDisponible = false;
                            break;
                        }
                    }
                }
                if (isDisponible) {
                    bornesDisponibles.add(borne);
                }
            }
        }
        return bornesDisponibles;
    }
} 