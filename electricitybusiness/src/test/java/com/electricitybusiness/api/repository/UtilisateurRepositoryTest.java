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
class UtilisateurRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Test
    void whenFindAll_thenReturnAllUtilisateurs() {
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

        entityManager.flush();

        // Act
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        // Assert
        assertThat(utilisateurs).hasSize(2);
        assertThat(utilisateurs).extracting(Utilisateur::getPseudo)
                .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void whenFindById_thenReturnUtilisateur() {
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
        entityManager.flush();

        // Act
        Optional<Utilisateur> found = utilisateurRepository.findById(utilisateur.getNumUtilisateur());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getPseudo()).isEqualTo("test.user");
        assertThat(found.get().getRole()).isEqualTo(RoleUtilisateur.CLIENT);
    }

    @Test
    void whenFindByPseudo_thenReturnUtilisateur() {
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
        entityManager.flush();

        // Act
        Optional<Utilisateur> found = utilisateurRepository.findByPseudo("test.user");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getNomUtilisateur()).isEqualTo("Test");
        assertThat(found.get().getPrenom()).isEqualTo("User");
    }

    @Test
    void whenFindByAdresseMail_thenReturnUtilisateur() {
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
        entityManager.flush();

        // Act
        Optional<Utilisateur> found = utilisateurRepository.findByAdresseMail("test@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getPseudo()).isEqualTo("test.user");
        assertThat(found.get().getRole()).isEqualTo(RoleUtilisateur.CLIENT);
    }

    @Test
    void whenFindByRole_thenReturnUtilisateursWithRole() {
        // Arrange
        Lieu lieu = new Lieu();
        lieu.setInstructions("Parking principal");
        entityManager.persist(lieu);

        Utilisateur client = new Utilisateur();
        client.setPseudo("client");
        client.setNomUtilisateur("Client");
        client.setPrenom("Test");
        client.setAdresseMail("client@example.com");
        client.setMotDePasse("password");
        client.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        client.setRole(RoleUtilisateur.CLIENT);
        client.setLieu(lieu);
        entityManager.persist(client);

        Utilisateur admin = new Utilisateur();
        admin.setPseudo("admin");
        admin.setNomUtilisateur("Admin");
        admin.setPrenom("Test");
        admin.setAdresseMail("admin@example.com");
        admin.setMotDePasse("password");
        admin.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        admin.setRole(RoleUtilisateur.ADMIN);
        admin.setLieu(lieu);
        entityManager.persist(admin);

        entityManager.flush();

        // Act
        List<Utilisateur> clients = utilisateurRepository.findByRole(RoleUtilisateur.CLIENT);

        // Assert
        assertThat(clients).hasSize(1);
        assertThat(clients).extracting(Utilisateur::getRole)
                .containsOnly(RoleUtilisateur.CLIENT);
    }

    @Test
    void whenFindByLieu_thenReturnUtilisateursInLieu() {
        // Arrange
        Lieu lieu1 = new Lieu();
        lieu1.setInstructions("Parking 1");
        entityManager.persist(lieu1);

        Lieu lieu2 = new Lieu();
        lieu2.setInstructions("Parking 2");
        entityManager.persist(lieu2);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setPseudo("user1");
        utilisateur1.setNomUtilisateur("User");
        utilisateur1.setPrenom("One");
        utilisateur1.setAdresseMail("user1@example.com");
        utilisateur1.setMotDePasse("password");
        utilisateur1.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur1.setRole(RoleUtilisateur.CLIENT);
        utilisateur1.setLieu(lieu1);
        entityManager.persist(utilisateur1);

        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setPseudo("user2");
        utilisateur2.setNomUtilisateur("User");
        utilisateur2.setPrenom("Two");
        utilisateur2.setAdresseMail("user2@example.com");
        utilisateur2.setMotDePasse("password");
        utilisateur2.setDateDeNaissance(java.time.LocalDate.of(1990, 1, 1));
        utilisateur2.setRole(RoleUtilisateur.CLIENT);
        utilisateur2.setLieu(lieu2);
        entityManager.persist(utilisateur2);

        entityManager.flush();

        // Act
        List<Utilisateur> lieuUtilisateurs = utilisateurRepository.findByLieu(lieu1);

        // Assert
        assertThat(lieuUtilisateurs).hasSize(1);
        assertThat(lieuUtilisateurs).extracting(Utilisateur::getLieu)
                .containsOnly(lieu1);
    }

    @Test
    void whenSave_thenPersistUtilisateur() {
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

        // Act
        Utilisateur saved = utilisateurRepository.save(utilisateur);

        // Assert
        assertThat(saved.getNumUtilisateur()).isNotNull();
        assertThat(saved.getPseudo()).isEqualTo("test.user");
        assertThat(saved.getRole()).isEqualTo(RoleUtilisateur.CLIENT);
        assertThat(saved.getLieu()).isEqualTo(lieu);
    }

    @Test
    void whenDelete_thenRemoveUtilisateur() {
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
        entityManager.flush();

        // Act
        utilisateurRepository.delete(utilisateur);
        Optional<Utilisateur> deleted = utilisateurRepository.findById(utilisateur.getNumUtilisateur());

        // Assert
        assertThat(deleted).isEmpty();
    }

    @Test
    void whenUpdate_thenModifyUtilisateur() {
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
        entityManager.flush();

        // Act
        utilisateur.setPseudo("updated.user");
        utilisateur.setRole(RoleUtilisateur.ADMIN);
        Utilisateur updated = utilisateurRepository.save(utilisateur);

        // Assert
        assertThat(updated.getPseudo()).isEqualTo("updated.user");
        assertThat(updated.getRole()).isEqualTo(RoleUtilisateur.ADMIN);
    }
} 