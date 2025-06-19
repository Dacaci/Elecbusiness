package com.electricitybusiness.api.repository;

import com.electricitybusiness.api.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LieuRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LieuRepository lieuRepository;

    @Test
    void whenFindAll_thenReturnAllLieux() {
        // Arrange
        Lieu lieu1 = new Lieu();
        lieu1.setInstructions("Parking 1");
        entityManager.persist(lieu1);

        Lieu lieu2 = new Lieu();
        lieu2.setInstructions("Parking 2");
        entityManager.persist(lieu2);

        entityManager.flush();

        // Act
        List<Lieu> lieux = lieuRepository.findAll();

        // Assert
        assertThat(lieux).hasSize(2);
        assertThat(lieux).extracting(Lieu::getInstructions)
                .containsExactlyInAnyOrder("Parking 1", "Parking 2");
    }

    @Test
    void whenFindById_thenReturnLieu() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);
        entityManager.flush();

        // Act
        Optional<Lieu> found = lieuRepository.findById(lieu.getNumLieu());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getInstructions()).isEqualTo("Parking principal");
    }

    @Test
    void whenSave_thenPersistLieu() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");

        // Act
        Lieu saved = lieuRepository.save(lieu);

        // Assert
        assertThat(saved.getNumLieu()).isNotNull();
        assertThat(saved.getInstructions()).isEqualTo("Parking principal");
    }

    @Test
    void whenDelete_thenRemoveLieu() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);
        entityManager.flush();

        // Act
        lieuRepository.delete(lieu);
        Optional<Lieu> deleted = lieuRepository.findById(lieu.getNumLieu());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyLieu() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);
        entityManager.flush();

        // Act
        lieu.setInstructions("Parking modifié");
        Lieu updated = lieuRepository.save(lieu);

        // Assert
        assertThat(updated.getInstructions()).isEqualTo("Parking modifié");
    }
} 