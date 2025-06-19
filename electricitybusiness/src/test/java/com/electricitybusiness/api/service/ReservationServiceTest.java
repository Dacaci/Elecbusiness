package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.repository.ReservationRepository;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation1;
    private Reservation reservation2;
    private Utilisateur utilisateur;
    private Borne borne;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setNumUtilisateur(1L);
        utilisateur.setNomUtilisateur("Dupont");
        utilisateur.setPrenom("Jean");

        borne = new Borne();
        borne.setNumBorne(1L);
        borne.setNomBorne("Borne-A001");
        borne.setPuissance(new BigDecimal("22.0"));

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        reservation1 = new Reservation();
        reservation1.setNumReservation(1L);
        reservation1.setDateDebut(dateDebut);
        reservation1.setDateFin(dateFin);
        reservation1.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation1.setEtat(EtatReservation.ACCEPTEE);
        reservation1.setMontantTotal(new BigDecimal("15.0"));
        reservation1.setUtilisateur(utilisateur);
        reservation1.setBorne(borne);

        reservation2 = new Reservation();
        reservation2.setNumReservation(2L);
        reservation2.setDateDebut(dateDebut.plusHours(3));
        reservation2.setDateFin(dateDebut.plusHours(5));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation2.setEtat(EtatReservation.ACCEPTEE);
        reservation2.setMontantTotal(new BigDecimal("15.0"));
        reservation2.setUtilisateur(utilisateur);
        reservation2.setBorne(borne);
    }

    @Test
    void findAll_ShouldReturnAllReservations() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // Act
        List<Reservation> result = reservationService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenReservationExists_ShouldReturnReservation() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation1));

        // Act
        Optional<Reservation> result = reservationService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(reservation1.getNumReservation(), result.get().getNumReservation());
        assertEquals(reservation1.getEtat(), result.get().getEtat());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenReservationDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Reservation> result = reservationService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(reservationRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedReservation() {
        // Arrange
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation1);

        // Act
        Reservation result = reservationService.save(reservation1);

        // Assert
        assertNotNull(result);
        assertEquals(reservation1.getNumReservation(), result.getNumReservation());
        assertEquals(reservation1.getEtat(), result.getEtat());
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    void update_WhenReservationExists_ShouldReturnUpdatedReservation() {
        // Arrange
        Reservation updatedReservation = new Reservation();
        updatedReservation.setNumReservation(1L);
        updatedReservation.setDateDebut(reservation1.getDateDebut());
        updatedReservation.setDateFin(reservation1.getDateFin().plusHours(2));
        updatedReservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        updatedReservation.setEtat(EtatReservation.ACCEPTEE);
        updatedReservation.setMontantTotal(new BigDecimal("30.0"));
        updatedReservation.setUtilisateur(utilisateur);
        updatedReservation.setBorne(borne);
        
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        // Act
        Reservation result = reservationService.update(1L, updatedReservation);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumReservation());
        assertEquals(new BigDecimal("30.0"), result.getMontantTotal());
        verify(reservationRepository, times(1)).save(updatedReservation);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        reservationService.deleteById(1L);

        // Assert
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenReservationExists_ShouldReturnTrue() {
        // Arrange
        when(reservationRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = reservationService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(reservationRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenReservationDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(reservationRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = reservationService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(reservationRepository, times(1)).existsById(999L);
    }
} 