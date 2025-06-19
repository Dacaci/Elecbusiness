package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void whenFindAll_thenReturnAllReservations() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation1 = new Reservation();
        reservation1.setDateDebut(dateDebut);
        reservation1.setDateFin(dateFin);
        reservation1.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation1.setEtat(EtatReservation.ACCEPTEE);
        reservation1.setMontantTotal(new BigDecimal("15.0"));
        reservation1.setUtilisateur(utilisateur);
        reservation1.setBorne(borne);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDateDebut(dateDebut.plusHours(3));
        reservation2.setDateFin(dateDebut.plusHours(5));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation2.setEtat(EtatReservation.ACCEPTEE);
        reservation2.setMontantTotal(new BigDecimal("15.0"));
        reservation2.setUtilisateur(utilisateur);
        reservation2.setBorne(borne);
        entityManager.persist(reservation2);

        entityManager.flush();

        // Act
        List<Reservation> reservations = reservationRepository.findAll();

        // Assert
        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getEtat)
                .containsOnly(EtatReservation.ACCEPTEE);
    }

    @Test
    void whenFindById_thenReturnReservation() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation.setEtat(EtatReservation.ACCEPTEE);
        reservation.setMontantTotal(new BigDecimal("15.0"));
        reservation.setUtilisateur(utilisateur);
        reservation.setBorne(borne);
        entityManager.persist(reservation);
        entityManager.flush();

        // Act
        Optional<Reservation> found = reservationRepository.findById(reservation.getNumReservation());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEtat()).isEqualTo(EtatReservation.ACCEPTEE);
        assertThat(found.get().getMontantTotal()).isEqualTo(new BigDecimal("15.0"));
    }

    @Test
    void whenFindByUtilisateur_thenReturnUserReservations() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setPseudo("user1");
        utilisateur1.setNomUtilisateur("User");
        utilisateur1.setPrenom("One");
        utilisateur1.setAdresseMail("user1@example.com");
        utilisateur1.setMotDePasse("password");
        utilisateur1.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur1.setRole(RoleUtilisateur.CLIENT);
        utilisateur1.setLieu(lieu);
        entityManager.persist(utilisateur1);

        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setPseudo("user2");
        utilisateur2.setNomUtilisateur("User");
        utilisateur2.setPrenom("Two");
        utilisateur2.setAdresseMail("user2@example.com");
        utilisateur2.setMotDePasse("password");
        utilisateur2.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur2.setRole(RoleUtilisateur.CLIENT);
        utilisateur2.setLieu(lieu);
        entityManager.persist(utilisateur2);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation1 = new Reservation();
        reservation1.setDateDebut(dateDebut);
        reservation1.setDateFin(dateFin);
        reservation1.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation1.setEtat(EtatReservation.ACCEPTEE);
        reservation1.setMontantTotal(new BigDecimal("15.0"));
        reservation1.setUtilisateur(utilisateur1);
        reservation1.setBorne(borne);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDateDebut(dateDebut.plusHours(3));
        reservation2.setDateFin(dateDebut.plusHours(5));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation2.setEtat(EtatReservation.ACCEPTEE);
        reservation2.setMontantTotal(new BigDecimal("15.0"));
        reservation2.setUtilisateur(utilisateur2);
        reservation2.setBorne(borne);
        entityManager.persist(reservation2);

        entityManager.flush();

        // Act
        List<Reservation> userReservations = reservationRepository.findByUtilisateur(utilisateur1);

        // Assert
        assertThat(userReservations).hasSize(1);
        assertThat(userReservations).extracting(Reservation::getUtilisateur)
                .containsOnly(utilisateur1);
    }

    @Test
    void whenFindByBorne_thenReturnBorneReservations() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne1 = new Borne();
        borne1.setNomBorne("Borne 1");
        borne1.setLatitude(new BigDecimal("48.8566"));
        borne1.setLongitude(new BigDecimal("2.3522"));
        borne1.setPuissance(new BigDecimal("22.0"));
        borne1.setEtat(EtatBorne.ACTIVE);
        borne1.setOccupee(false);
        borne1.setSurPied(true);
        borne1.setLieu(lieu);
        entityManager.persist(borne1);

        Borne borne2 = new Borne();
        borne2.setNomBorne("Borne 2");
        borne2.setLatitude(new BigDecimal("48.8567"));
        borne2.setLongitude(new BigDecimal("2.3523"));
        borne2.setPuissance(new BigDecimal("50.0"));
        borne2.setEtat(EtatBorne.ACTIVE);
        borne2.setOccupee(false);
        borne2.setSurPied(true);
        borne2.setLieu(lieu);
        entityManager.persist(borne2);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation1 = new Reservation();
        reservation1.setDateDebut(dateDebut);
        reservation1.setDateFin(dateFin);
        reservation1.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation1.setEtat(EtatReservation.ACCEPTEE);
        reservation1.setMontantTotal(new BigDecimal("15.0"));
        reservation1.setUtilisateur(utilisateur);
        reservation1.setBorne(borne1);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDateDebut(dateDebut.plusHours(3));
        reservation2.setDateFin(dateDebut.plusHours(5));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation2.setEtat(EtatReservation.ACCEPTEE);
        reservation2.setMontantTotal(new BigDecimal("15.0"));
        reservation2.setUtilisateur(utilisateur);
        reservation2.setBorne(borne2);
        entityManager.persist(reservation2);

        entityManager.flush();

        // Act
        List<Reservation> borneReservations = reservationRepository.findByBorne(borne1);

        // Assert
        assertThat(borneReservations).hasSize(1);
        assertThat(borneReservations).extracting(Reservation::getBorne)
                .containsOnly(borne1);
    }

    @Test
    void whenFindByEtat_thenReturnReservationsWithState() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation1 = new Reservation();
        reservation1.setDateDebut(dateDebut);
        reservation1.setDateFin(dateFin);
        reservation1.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation1.setEtat(EtatReservation.ACCEPTEE);
        reservation1.setMontantTotal(new BigDecimal("15.0"));
        reservation1.setUtilisateur(utilisateur);
        reservation1.setBorne(borne);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setDateDebut(dateDebut.plusHours(3));
        reservation2.setDateFin(dateDebut.plusHours(5));
        reservation2.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation2.setEtat(EtatReservation.DEMANDE);
        reservation2.setMontantTotal(new BigDecimal("15.0"));
        reservation2.setUtilisateur(utilisateur);
        reservation2.setBorne(borne);
        entityManager.persist(reservation2);

        entityManager.flush();

        // Act
        List<Reservation> acceptedReservations = reservationRepository.findByEtat(EtatReservation.ACCEPTEE);

        // Assert
        assertThat(acceptedReservations).hasSize(1);
        assertThat(acceptedReservations).extracting(Reservation::getEtat)
                .containsOnly(EtatReservation.ACCEPTEE);
    }

    @Test
    void whenFindByUtilisateurAndDateFinBefore_thenReturnPastReservations() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastDate = now.minusDays(1);
        LocalDateTime futureDate = now.plusDays(1);

        Reservation pastReservation = new Reservation();
        pastReservation.setDateDebut(pastDate.minusHours(2));
        pastReservation.setDateFin(pastDate);
        pastReservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        pastReservation.setEtat(EtatReservation.TERMINEE);
        pastReservation.setMontantTotal(new BigDecimal("15.0"));
        pastReservation.setUtilisateur(utilisateur);
        pastReservation.setBorne(borne);
        entityManager.persist(pastReservation);

        Reservation futureReservation = new Reservation();
        futureReservation.setDateDebut(futureDate);
        futureReservation.setDateFin(futureDate.plusHours(2));
        futureReservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        futureReservation.setEtat(EtatReservation.ACCEPTEE);
        futureReservation.setMontantTotal(new BigDecimal("15.0"));
        futureReservation.setUtilisateur(utilisateur);
        futureReservation.setBorne(borne);
        entityManager.persist(futureReservation);

        entityManager.flush();

        // Act
        List<Reservation> pastReservations = reservationRepository.findByUtilisateurAndDateFinBefore(utilisateur, now);

        // Assert
        assertThat(pastReservations).hasSize(1);
        assertThat(pastReservations).extracting(Reservation::getDateFin)
                .allMatch(date -> date.isBefore(now));
    }

    @Test
    void whenSave_thenPersistReservation() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation.setEtat(EtatReservation.ACCEPTEE);
        reservation.setMontantTotal(new BigDecimal("15.0"));
        reservation.setUtilisateur(utilisateur);
        reservation.setBorne(borne);

        // Act
        Reservation saved = reservationRepository.save(reservation);

        // Assert
        assertThat(saved.getNumReservation()).isNotNull();
        assertThat(saved.getEtat()).isEqualTo(EtatReservation.ACCEPTEE);
        assertThat(saved.getUtilisateur()).isEqualTo(utilisateur);
        assertThat(saved.getBorne()).isEqualTo(borne);
    }

    @Test
    void whenDelete_thenRemoveReservation() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation.setEtat(EtatReservation.ACCEPTEE);
        reservation.setMontantTotal(new BigDecimal("15.0"));
        reservation.setUtilisateur(utilisateur);
        reservation.setBorne(borne);
        entityManager.persist(reservation);
        entityManager.flush();

        // Act
        reservationRepository.delete(reservation);
        Optional<Reservation> deleted = reservationRepository.findById(reservation.getNumReservation());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyReservation() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("test.user");
        utilisateur.setNomUtilisateur("Test");
        utilisateur.setPrenom("User");
        utilisateur.setAdresseMail("test@example.com");
        utilisateur.setMotDePasse("password");
        utilisateur.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur.setRole(RoleUtilisateur.CLIENT);
        utilisateur.setLieu(lieu);
        entityManager.persist(utilisateur);

        Borne borne = new Borne();
        borne.setNomBorne("Borne test");
        borne.setLatitude(new BigDecimal("48.8566"));
        borne.setLongitude(new BigDecimal("2.3522"));
        borne.setPuissance(new BigDecimal("22.0"));
        borne.setEtat(EtatBorne.ACTIVE);
        borne.setOccupee(false);
        borne.setSurPied(true);
        borne.setLieu(lieu);
        entityManager.persist(borne);

        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setPrixMinuteHisto(new BigDecimal("0.004167"));
        reservation.setEtat(EtatReservation.ACCEPTEE);
        reservation.setMontantTotal(new BigDecimal("15.0"));
        reservation.setUtilisateur(utilisateur);
        reservation.setBorne(borne);
        entityManager.persist(reservation);
        entityManager.flush();

        // Act
        reservation.setEtat(EtatReservation.TERMINEE);
        reservation.setMontantTotal(new BigDecimal("30.0"));
        Reservation updated = reservationRepository.save(reservation);

        // Assert
        assertThat(updated.getEtat()).isEqualTo(EtatReservation.TERMINEE);
        assertThat(updated.getMontantTotal()).isEqualTo(new BigDecimal("30.0"));
    }
} 