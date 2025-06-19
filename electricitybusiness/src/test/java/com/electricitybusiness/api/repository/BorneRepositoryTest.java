package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BorneRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BorneRepository borneRepository;

    @Test
    void whenFindAll_thenReturnAllBornes() {
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

        entityManager.flush();

        // Act
        List<Borne> bornes = borneRepository.findAll();

        // Assert
        assertThat(bornes).hasSize(2);
        assertThat(bornes).extracting(Borne::getEtat)
                .containsOnly(EtatBorne.ACTIVE);
    }

    @Test
    void whenFindById_thenReturnBorne() {
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
        entityManager.flush();

        // Act
        Optional<Borne> found = borneRepository.findById(borne.getNumBorne());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNomBorne()).isEqualTo("Borne test");
        assertThat(found.get().getEtat()).isEqualTo(EtatBorne.ACTIVE);
    }

    @Test
    void whenFindByLieu_thenReturnBornesInLieu() {
        // Arrange
        Lieu lieu1 = new Lieu();
        lieu1.setInstructions("Parking 1");
        entityManager.persist(lieu1);

        Lieu lieu2 = new Lieu();
        lieu2.setInstructions("Parking 2");
        entityManager.persist(lieu2);

        Borne borne1 = new Borne();
        borne1.setNomBorne("Borne 1");
        borne1.setLatitude(new BigDecimal("48.8566"));
        borne1.setLongitude(new BigDecimal("2.3522"));
        borne1.setPuissance(new BigDecimal("22.0"));
        borne1.setEtat(EtatBorne.ACTIVE);
        borne1.setOccupee(false);
        borne1.setSurPied(true);
        borne1.setLieu(lieu1);
        entityManager.persist(borne1);

        Borne borne2 = new Borne();
        borne2.setNomBorne("Borne 2");
        borne2.setLatitude(new BigDecimal("48.8567"));
        borne2.setLongitude(new BigDecimal("2.3523"));
        borne2.setPuissance(new BigDecimal("50.0"));
        borne2.setEtat(EtatBorne.ACTIVE);
        borne2.setOccupee(false);
        borne2.setSurPied(true);
        borne2.setLieu(lieu2);
        entityManager.persist(borne2);

        entityManager.flush();

        // Act
        List<Borne> lieuBornes = borneRepository.findByLieu(lieu1);

        // Assert
        assertThat(lieuBornes).hasSize(1);
        assertThat(lieuBornes).extracting(Borne::getLieu)
                .containsOnly(lieu1);
    }

    @Test
    void whenFindByEtat_thenReturnBornesWithState() {
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
        borne2.setEtat(EtatBorne.INACTIVE);
        borne2.setOccupee(false);
        borne2.setSurPied(true);
        borne2.setLieu(lieu);
        entityManager.persist(borne2);

        entityManager.flush();

        // Act
        List<Borne> activeBornes = borneRepository.findByEtat(EtatBorne.ACTIVE);

        // Assert
        assertThat(activeBornes).hasSize(1);
        assertThat(activeBornes).extracting(Borne::getEtat)
                .containsOnly(EtatBorne.ACTIVE);
    }

    @Test
    void whenFindByOccupee_thenReturnBornesWithOccupation() {
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
        borne1.setOccupee(true);
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

        entityManager.flush();

        // Act
        List<Borne> occupiedBornes = borneRepository.findByOccupee(true);

        // Assert
        assertThat(occupiedBornes).hasSize(1);
        assertThat(occupiedBornes).extracting(Borne::getOccupee)
                .containsOnly(true);
    }

    @Test
    void whenSave_thenPersistBorne() {
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

        // Act
        Borne saved = borneRepository.save(borne);

        // Assert
        assertThat(saved.getNumBorne()).isNotNull();
        assertThat(saved.getNomBorne()).isEqualTo("Borne test");
        assertThat(saved.getEtat()).isEqualTo(EtatBorne.ACTIVE);
        assertThat(saved.getLieu()).isEqualTo(lieu);
    }

    @Test
    void whenDelete_thenRemoveBorne() {
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
        entityManager.flush();

        // Act
        borneRepository.delete(borne);
        Optional<Borne> deleted = borneRepository.findById(borne.getNumBorne());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyBorne() {
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
        entityManager.flush();

        // Act
        borne.setEtat(EtatBorne.INACTIVE);
        borne.setOccupee(true);
        Borne updated = borneRepository.save(borne);

        // Assert
        assertThat(updated.getEtat()).isEqualTo(EtatBorne.INACTIVE);
        assertThat(updated.getNomBorne()).isEqualTo("Borne modifiée");
        assertThat(updated.getPuissance()).isEqualTo(new BigDecimal("50.0"));
    }
} 