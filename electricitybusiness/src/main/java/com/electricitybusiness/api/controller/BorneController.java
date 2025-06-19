package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.model.Borne;
import com.electricitybusiness.api.service.BorneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des bornes électriques.
 * Expose les endpoints pour les opérations CRUD sur les bornes.
 */
@RestController
@RequestMapping("/api/bornes")
@RequiredArgsConstructor
public class BorneController {

    private final BorneService borneService;

    /**
     * Récupère toutes les bornes.
     * GET /api/bornes
     */
    @GetMapping
    public ResponseEntity<List<Borne>> getAllBornes() {
        List<Borne> bornes = borneService.findAll();
        return ResponseEntity.ok(bornes);
    }

    /**
     * Récupère une borne par son ID.
     * GET /api/bornes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Borne> getBorneById(@PathVariable Long id) {
        return borneService.findById(id)
                .map(borne -> ResponseEntity.ok(borne))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle borne.
     * POST /api/bornes
     */
    @PostMapping
    public ResponseEntity<Borne> createBorne(@Valid @RequestBody Borne borne) {
        Borne savedBorne = borneService.save(borne);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBorne);
    }

    /**
     * Met à jour une borne existante.
     * PUT /api/bornes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Borne> updateBorne(@PathVariable Long id, @Valid @RequestBody Borne borne) {
        if (!borneService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Borne updatedBorne = borneService.update(id, borne);
        return ResponseEntity.ok(updatedBorne);
    }

    /**
     * Supprime une borne.
     * DELETE /api/bornes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorne(@PathVariable Long id) {
        if (!borneService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        borneService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère les bornes à proximité d'une position donnée.
     * GET /api/bornes/proximite?longitude=...&latitude=...&rayon=...
     */
    @GetMapping("/proximite")
    public ResponseEntity<List<Borne>> getBornesProches(@RequestParam double longitude,
                                                       @RequestParam double latitude,
                                                       @RequestParam double rayon) {
        List<Borne> bornesProches = borneService.findNearbyBornes(longitude, latitude, rayon);
        return ResponseEntity.ok(bornesProches);
    }

    /**
     * Recherche les bornes disponibles dans une ville à une date donnée (créneau).
     * GET /api/bornes/disponibles?ville=...&dateDebut=...&dateFin=...
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Borne>> getAvailableBornesInCityAtDate(@RequestParam String ville,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
                                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        List<Borne> bornes = borneService.findAvailableBornesInCityAtDate(ville, dateDebut, dateFin);
        return ResponseEntity.ok(bornes);
    }
} 