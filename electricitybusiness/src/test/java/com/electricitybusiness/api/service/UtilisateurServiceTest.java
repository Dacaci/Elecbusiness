package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur1;
    private Utilisateur utilisateur2;
    private Lieu lieu;

    @BeforeEach
    void setUp() {
        lieu = new Lieu();
        lieu.setNumLieu(1L);
        lieu.setInstructions("Parking principal");

        utilisateur1 = new Utilisateur();
        utilisateur1.setNumUtilisateur(1L);
        utilisateur1.setPseudo("jean.dupont");
        utilisateur1.setNomUtilisateur("Dupont");
        utilisateur1.setPrenom("Jean");
        utilisateur1.setAdresseMail("jean.dupont@email.com");
        utilisateur1.setRole(RoleUtilisateur.CLIENT);
        utilisateur1.setDateDeNaissance(LocalDate.of(1990, 5, 15));
        utilisateur1.setCompteValide(true);
        utilisateur1.setBanni(false);
        utilisateur1.setLieu(lieu);
        utilisateur1.setMotDePasse("plainPassword");

        utilisateur2 = new Utilisateur();
        utilisateur2.setNumUtilisateur(2L);
        utilisateur2.setPseudo("marie.martin");
        utilisateur2.setNomUtilisateur("Martin");
        utilisateur2.setPrenom("Marie");
        utilisateur2.setAdresseMail("marie.martin@email.com");
        utilisateur2.setRole(RoleUtilisateur.CLIENT);
        utilisateur2.setDateDeNaissance(LocalDate.of(1985, 8, 20));
        utilisateur2.setCompteValide(true);
        utilisateur2.setBanni(false);
        utilisateur2.setLieu(lieu);
        utilisateur2.setMotDePasse("anotherPlainPassword");
    }

    @Test
    void findAll_ShouldReturnAllUtilisateurs() {
        // Arrange
        when(utilisateurRepository.findAll()).thenReturn(Arrays.asList(utilisateur1, utilisateur2));

        // Act
        List<Utilisateur> result = utilisateurService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenUtilisateurExists_ShouldReturnUtilisateur() {
        // Arrange
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur1));

        // Act
        Optional<Utilisateur> result = utilisateurService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(utilisateur1.getNumUtilisateur(), result.get().getNumUtilisateur());
        assertEquals(utilisateur1.getPseudo(), result.get().getPseudo());
        verify(utilisateurRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenUtilisateurDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(utilisateurRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Utilisateur> result = utilisateurService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(utilisateurRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedUtilisateur() {
        // Arrange
        when(passwordEncoder.encode(utilisateur1.getMotDePasse())).thenReturn("encodedPassword");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur1);

        // Act
        Utilisateur result = utilisateurService.save(utilisateur1);

        // Assert
        assertNotNull(result);
        assertEquals(utilisateur1.getNumUtilisateur(), result.getNumUtilisateur());
        assertEquals(utilisateur1.getPseudo(), result.getPseudo());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(utilisateurRepository, times(1)).save(utilisateur1);
    }

    @Test
    void update_WhenUtilisateurExists_ShouldReturnUpdatedUtilisateur() {
        // Arrange
        Utilisateur updatedUtilisateur = new Utilisateur();
        updatedUtilisateur.setNumUtilisateur(1L);
        updatedUtilisateur.setPseudo("jean.dupont.updated");
        updatedUtilisateur.setNomUtilisateur("Dupont");
        updatedUtilisateur.setPrenom("Jean");
        updatedUtilisateur.setAdresseMail("jean.dupont.updated@email.com");
        updatedUtilisateur.setRole(RoleUtilisateur.CLIENT);
        updatedUtilisateur.setDateDeNaissance(LocalDate.of(1990, 5, 15));
        updatedUtilisateur.setCompteValide(true);
        updatedUtilisateur.setBanni(false);
        updatedUtilisateur.setLieu(lieu);
        updatedUtilisateur.setMotDePasse("newPlainPassword");
        
        when(passwordEncoder.encode(updatedUtilisateur.getMotDePasse())).thenReturn("newEncodedPassword");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(updatedUtilisateur);

        // Act
        Utilisateur result = utilisateurService.update(1L, updatedUtilisateur);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumUtilisateur());
        assertEquals("jean.dupont.updated", result.getPseudo());
        verify(passwordEncoder, times(1)).encode("newPlainPassword");
        verify(utilisateurRepository, times(1)).save(updatedUtilisateur);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        utilisateurService.deleteById(1L);

        // Assert
        verify(utilisateurRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenUtilisateurExists_ShouldReturnTrue() {
        // Arrange
        when(utilisateurRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = utilisateurService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(utilisateurRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenUtilisateurDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(utilisateurRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = utilisateurService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(utilisateurRepository, times(1)).existsById(999L);
    }
} 