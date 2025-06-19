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
class AdresseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdresseRepository adresseRepository;

    @Test
    void whenFindAll_thenReturnAllAdresses() {
        // Arrange
        Adresse adresse1 = new Adresse();
        adresse1.setNomAdresse("Adresse principale");
        adresse1.setNumeroEtRue("123 rue de la Paix");
        adresse1.setCodePostal("75001");
        adresse1.setVille("Paris");
        adresse1.setPays("France");
        entityManager.persist(adresse1);

        Adresse adresse2 = new Adresse();
        adresse2.setNomAdresse("Adresse secondaire");
        adresse2.setNumeroEtRue("456 avenue des Champs-Élysées");
        adresse2.setCodePostal("75008");
        adresse2.setVille("Paris");
        adresse2.setPays("France");
        entityManager.persist(adresse2);

        entityManager.flush();

        // Act
        List<Adresse> adresses = adresseRepository.findAll();

        // Assert
        assertThat(adresses).hasSize(2);
        assertThat(adresses).extracting(Adresse::getVille)
                .containsOnly("Paris");
    }

    @Test
    void whenFindById_thenReturnAdresse() {
        // Arrange
        Adresse adresse = new Adresse();
        adresse.setNomAdresse("Adresse test");
        adresse.setNumeroEtRue("123 rue de la Paix");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");
        adresse.setPays("France");
        entityManager.persist(adresse);
        entityManager.flush();

        // Act
        Optional<Adresse> found = adresseRepository.findById(adresse.getNumAdresse());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNumeroEtRue()).isEqualTo("123 rue de la Paix");
        assertThat(found.get().getVille()).isEqualTo("Paris");
    }

    @Test
    void whenFindByVille_thenReturnAdressesInCity() {
        // Arrange
        Adresse adresse1 = new Adresse();
        adresse1.setNomAdresse("Adresse Paris 1");
        adresse1.setNumeroEtRue("123 rue de la Paix");
        adresse1.setCodePostal("75001");
        adresse1.setVille("Paris");
        adresse1.setPays("France");
        entityManager.persist(adresse1);

        Adresse adresse2 = new Adresse();
        adresse2.setNomAdresse("Adresse Paris 2");
        adresse2.setNumeroEtRue("456 avenue des Champs-Élysées");
        adresse2.setCodePostal("75008");
        adresse2.setVille("Paris");
        adresse2.setPays("France");
        entityManager.persist(adresse2);

        Adresse adresse3 = new Adresse();
        adresse3.setNomAdresse("Adresse Lyon");
        adresse3.setNumeroEtRue("789 rue de la République");
        adresse3.setCodePostal("69001");
        adresse3.setVille("Lyon");
        adresse3.setPays("France");
        entityManager.persist(adresse3);

        entityManager.flush();

        // Act
        List<Adresse> parisAdresses = adresseRepository.findByVille("Paris");

        // Assert
        assertThat(parisAdresses).hasSize(2);
        assertThat(parisAdresses).extracting(Adresse::getVille)
                .containsOnly("Paris");
    }

    @Test
    void whenSave_thenPersistAdresse() {
        // Arrange
        Adresse adresse = new Adresse();
        adresse.setNomAdresse("Nouvelle adresse");
        adresse.setNumeroEtRue("123 rue nouvelle");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // Act
        Adresse saved = adresseRepository.save(adresse);

        // Assert
        assertThat(saved.getNumAdresse()).isNotNull();
        assertThat(saved.getNomAdresse()).isEqualTo("Nouvelle adresse");
        assertThat(saved.getVille()).isEqualTo("Paris");
    }

    @Test
    void whenDelete_thenRemoveAdresse() {
        // Arrange
        Adresse adresse = new Adresse();
        adresse.setNomAdresse("Adresse à supprimer");
        adresse.setNumeroEtRue("123 rue à supprimer");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");
        adresse.setPays("France");
        entityManager.persist(adresse);
        entityManager.flush();

        // Act
        adresseRepository.delete(adresse);
        Optional<Adresse> deleted = adresseRepository.findById(adresse.getNumAdresse());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyAdresse() {
        // Arrange
        Adresse adresse = new Adresse();
        adresse.setNomAdresse("Adresse originale");
        adresse.setNumeroEtRue("123 rue originale");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");
        adresse.setPays("France");
        entityManager.persist(adresse);
        entityManager.flush();

        // Act
        adresse.setNomAdresse("Adresse modifiée");
        adresse.setNumeroEtRue("123 rue modifiée");
        Adresse updated = adresseRepository.save(adresse);

        // Assert
        assertThat(updated.getNomAdresse()).isEqualTo("Adresse modifiée");
        assertThat(updated.getNumeroEtRue()).isEqualTo("123 rue modifiée");
    }
} 