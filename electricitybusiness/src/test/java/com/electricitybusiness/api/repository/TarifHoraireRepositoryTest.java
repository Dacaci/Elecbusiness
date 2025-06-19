package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TarifHoraireRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TarifHoraireRepository tarifHoraireRepository;

    @Test
    void whenFindAll_thenReturnAllTarifs() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif1 = new TarifHoraire();
        tarif1.setTarifParMinute(new BigDecimal("0.004167"));
        tarif1.setDateDebut(LocalDate.now());
        tarif1.setDateFin(LocalDate.now().plusMonths(1));
        tarif1.setHeureDebut(LocalTime.of(8, 0));
        tarif1.setHeureFin(LocalTime.of(20, 0));
        tarif1.setActif(true);
        tarif1.setBorne(borne);
        entityManager.persist(tarif1);

        TarifHoraire tarif2 = new TarifHoraire();
        tarif2.setTarifParMinute(new BigDecimal("0.003333"));
        tarif2.setDateDebut(LocalDate.now());
        tarif2.setDateFin(LocalDate.now().plusMonths(1));
        tarif2.setHeureDebut(LocalTime.of(20, 0));
        tarif2.setHeureFin(LocalTime.of(8, 0));
        tarif2.setActif(true);
        tarif2.setBorne(borne);
        entityManager.persist(tarif2);

        entityManager.flush();

        // Act
        List<TarifHoraire> tarifs = tarifHoraireRepository.findAll();

        // Assert
        assertThat(tarifs).hasSize(2);
        assertThat(tarifs).extracting(TarifHoraire::getTarifParMinute)
                .containsExactlyInAnyOrder(new BigDecimal("0.004167"), new BigDecimal("0.003333"));
    }

    @Test
    void whenFindById_thenReturnTarif() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif = new TarifHoraire();
        tarif.setTarifParMinute(new BigDecimal("0.004167"));
        tarif.setDateDebut(LocalDate.now());
        tarif.setDateFin(LocalDate.now().plusMonths(1));
        tarif.setHeureDebut(LocalTime.of(8, 0));
        tarif.setHeureFin(LocalTime.of(20, 0));
        tarif.setActif(true);
        tarif.setBorne(borne);
        entityManager.persist(tarif);
        entityManager.flush();

        // Act
        Optional<TarifHoraire> found = tarifHoraireRepository.findById(tarif.getNumTarif());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getTarifParMinute()).isEqualTo(new BigDecimal("0.004167"));
        assertThat(found.get().getBorne()).isEqualTo(borne);
    }

    @Test
    void whenFindByBorne_thenReturnBorneTarifs() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif1 = new TarifHoraire();
        tarif1.setTarifParMinute(new BigDecimal("0.004167"));
        tarif1.setDateDebut(LocalDate.now());
        tarif1.setDateFin(LocalDate.now().plusMonths(1));
        tarif1.setHeureDebut(LocalTime.of(8, 0));
        tarif1.setHeureFin(LocalTime.of(20, 0));
        tarif1.setActif(true);
        tarif1.setBorne(borne1);
        entityManager.persist(tarif1);

        TarifHoraire tarif2 = new TarifHoraire();
        tarif2.setTarifParMinute(new BigDecimal("0.003333"));
        tarif2.setDateDebut(LocalDate.now());
        tarif2.setDateFin(LocalDate.now().plusMonths(1));
        tarif2.setHeureDebut(LocalTime.of(20, 0));
        tarif2.setHeureFin(LocalTime.of(8, 0));
        tarif2.setActif(true);
        tarif2.setBorne(borne2);
        entityManager.persist(tarif2);

        entityManager.flush();

        // Act
        List<TarifHoraire> borneTarifs = tarifHoraireRepository.findByBorne(borne1);

        // Assert
        assertThat(borneTarifs).hasSize(1);
        assertThat(borneTarifs).extracting(TarifHoraire::getBorne)
                .containsOnly(borne1);
    }

    @Test
    void whenFindByActif_thenReturnActiveTarifs() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif1 = new TarifHoraire();
        tarif1.setTarifParMinute(new BigDecimal("0.004167"));
        tarif1.setDateDebut(LocalDate.now());
        tarif1.setDateFin(LocalDate.now().plusMonths(1));
        tarif1.setHeureDebut(LocalTime.of(8, 0));
        tarif1.setHeureFin(LocalTime.of(20, 0));
        tarif1.setActif(true);
        tarif1.setBorne(borne);
        entityManager.persist(tarif1);

        TarifHoraire tarif2 = new TarifHoraire();
        tarif2.setTarifParMinute(new BigDecimal("0.003333"));
        tarif2.setDateDebut(LocalDate.now());
        tarif2.setDateFin(LocalDate.now().plusMonths(1));
        tarif2.setHeureDebut(LocalTime.of(20, 0));
        tarif2.setHeureFin(LocalTime.of(8, 0));
        tarif2.setActif(false);
        tarif2.setBorne(borne);
        entityManager.persist(tarif2);

        entityManager.flush();

        // Act
        List<TarifHoraire> activeTarifs = tarifHoraireRepository.findByActif(true);

        // Assert
        assertThat(activeTarifs).hasSize(1);
        assertThat(activeTarifs).extracting(TarifHoraire::getActif)
                .containsOnly(true);
    }

    @Test
    void whenSave_thenPersistTarif() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif = new TarifHoraire();
        tarif.setTarifParMinute(new BigDecimal("0.004167"));
        tarif.setDateDebut(LocalDate.now());
        tarif.setDateFin(LocalDate.now().plusMonths(1));
        tarif.setHeureDebut(LocalTime.of(8, 0));
        tarif.setHeureFin(LocalTime.of(20, 0));
        tarif.setActif(true);
        tarif.setBorne(borne);

        // Act
        TarifHoraire saved = tarifHoraireRepository.save(tarif);

        // Assert
        assertThat(saved.getNumTarif()).isNotNull();
        assertThat(saved.getTarifParMinute()).isEqualTo(new BigDecimal("0.004167"));
        assertThat(saved.getBorne()).isEqualTo(borne);
    }

    @Test
    void whenDelete_thenRemoveTarif() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif = new TarifHoraire();
        tarif.setTarifParMinute(new BigDecimal("0.004167"));
        tarif.setDateDebut(LocalDate.now());
        tarif.setDateFin(LocalDate.now().plusMonths(1));
        tarif.setHeureDebut(LocalTime.of(8, 0));
        tarif.setHeureFin(LocalTime.of(20, 0));
        tarif.setActif(true);
        tarif.setBorne(borne);
        entityManager.persist(tarif);
        entityManager.flush();

        // Act
        tarifHoraireRepository.delete(tarif);
        Optional<TarifHoraire> deleted = tarifHoraireRepository.findById(tarif.getNumTarif());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyTarif() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

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

        TarifHoraire tarif = new TarifHoraire();
        tarif.setTarifParMinute(new BigDecimal("0.004167"));
        tarif.setDateDebut(LocalDate.now());
        tarif.setDateFin(LocalDate.now().plusMonths(1));
        tarif.setHeureDebut(LocalTime.of(8, 0));
        tarif.setHeureFin(LocalTime.of(20, 0));
        tarif.setActif(true);
        tarif.setBorne(borne);
        entityManager.persist(tarif);
        entityManager.flush();

        // Act
        tarif.setTarifParMinute(new BigDecimal("0.005000"));
        tarif.setActif(false);
        TarifHoraire updated = tarifHoraireRepository.save(tarif);

        // Assert
        assertThat(updated.getTarifParMinute()).isEqualTo(new BigDecimal("0.005000"));
        assertThat(updated.getActif()).isFalse();
    }
} 