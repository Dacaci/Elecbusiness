package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Adresse;
import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.repository.AdresseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdresseServiceTest {

    @Mock
    private AdresseRepository adresseRepository;

    @InjectMocks
    private AdresseService adresseService;

    private Adresse adresse1;
    private Adresse adresse2;
    private Lieu lieu;

    @BeforeEach
    void setUp() {
        lieu = new Lieu();
        lieu.setNumLieu(1L);
        lieu.setInstructions("Parking principal");

        adresse1 = new Adresse();
        adresse1.setNumAdresse(1L);
        adresse1.setNomAdresse("Adresse principale");
        adresse1.setNumeroEtRue("123 rue de la Paix");
        adresse1.setCodePostal("75001");
        adresse1.setVille("Paris");
        adresse1.setPays("France");
        adresse1.setLieu(lieu);

        adresse2 = new Adresse();
        adresse2.setNumAdresse(2L);
        adresse2.setNomAdresse("Adresse secondaire");
        adresse2.setNumeroEtRue("456 avenue des Champs-Élysées");
        adresse2.setCodePostal("75008");
        adresse2.setVille("Paris");
        adresse2.setPays("France");
        adresse2.setLieu(lieu);
    }

    @Test
    void findAll_ShouldReturnAllAdresses() {
        // Arrange
        when(adresseRepository.findAll()).thenReturn(Arrays.asList(adresse1, adresse2));

        // Act
        List<Adresse> result = adresseService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(adresseRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenAdresseExists_ShouldReturnAdresse() {
        // Arrange
        when(adresseRepository.findById(1L)).thenReturn(Optional.of(adresse1));

        // Act
        Optional<Adresse> result = adresseService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(adresse1.getNumAdresse(), result.get().getNumAdresse());
        assertEquals(adresse1.getNomAdresse(), result.get().getNomAdresse());
        verify(adresseRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenAdresseDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(adresseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Adresse> result = adresseService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(adresseRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedAdresse() {
        // Arrange
        when(adresseRepository.save(any(Adresse.class))).thenReturn(adresse1);

        // Act
        Adresse result = adresseService.save(adresse1);

        // Assert
        assertNotNull(result);
        assertEquals(adresse1.getNumAdresse(), result.getNumAdresse());
        assertEquals(adresse1.getNomAdresse(), result.getNomAdresse());
        verify(adresseRepository, times(1)).save(adresse1);
    }

    @Test
    void update_WhenAdresseExists_ShouldReturnUpdatedAdresse() {
        // Arrange
        Adresse updatedAdresse = new Adresse();
        updatedAdresse.setNumAdresse(1L);
        updatedAdresse.setNomAdresse("Nouvelle adresse");
        updatedAdresse.setNumeroEtRue("789 rue de Rivoli");
        updatedAdresse.setCodePostal("75004");
        updatedAdresse.setVille("Paris");
        updatedAdresse.setPays("France");
        updatedAdresse.setLieu(lieu);
        
        when(adresseRepository.save(any(Adresse.class))).thenReturn(updatedAdresse);

        // Act
        Adresse result = adresseService.update(1L, updatedAdresse);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumAdresse());
        assertEquals("Nouvelle adresse", result.getNomAdresse());
        verify(adresseRepository, times(1)).save(updatedAdresse);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        adresseService.deleteById(1L);

        // Assert
        verify(adresseRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenAdresseExists_ShouldReturnTrue() {
        // Arrange
        when(adresseRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = adresseService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(adresseRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenAdresseDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(adresseRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = adresseService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(adresseRepository, times(1)).existsById(999L);
    }
} 