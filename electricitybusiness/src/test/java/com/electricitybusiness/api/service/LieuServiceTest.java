package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.repository.LieuRepository;
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
class LieuServiceTest {

    @Mock
    private LieuRepository lieuRepository;

    @InjectMocks
    private LieuService lieuService;

    private Lieu lieu1;
    private Lieu lieu2;

    @BeforeEach
    void setUp() {
        lieu1 = new Lieu();
        lieu1.setNumLieu(1L);
        lieu1.setInstructions("Parking principal");

        lieu2 = new Lieu();
        lieu2.setNumLieu(2L);
        lieu2.setInstructions("Parking secondaire");
    }

    @Test
    void findAll_ShouldReturnAllLieux() {
        // Arrange
        when(lieuRepository.findAll()).thenReturn(Arrays.asList(lieu1, lieu2));

        // Act
        List<Lieu> result = lieuService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lieuRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenLieuExists_ShouldReturnLieu() {
        // Arrange
        when(lieuRepository.findById(1L)).thenReturn(Optional.of(lieu1));

        // Act
        Optional<Lieu> result = lieuService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(lieu1.getNumLieu(), result.get().getNumLieu());
        assertEquals(lieu1.getInstructions(), result.get().getInstructions());
        verify(lieuRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenLieuDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(lieuRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Lieu> result = lieuService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(lieuRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedLieu() {
        // Arrange
        when(lieuRepository.save(any(Lieu.class))).thenReturn(lieu1);

        // Act
        Lieu result = lieuService.save(lieu1);

        // Assert
        assertNotNull(result);
        assertEquals(lieu1.getNumLieu(), result.getNumLieu());
        assertEquals(lieu1.getInstructions(), result.getInstructions());
        verify(lieuRepository, times(1)).save(lieu1);
    }

    @Test
    void update_WhenLieuExists_ShouldReturnUpdatedLieu() {
        // Arrange
        Lieu updatedLieu = new Lieu();
        updatedLieu.setNumLieu(1L);
        updatedLieu.setInstructions("Nouvelles instructions");
        when(lieuRepository.save(any(Lieu.class))).thenReturn(updatedLieu);

        // Act
        Lieu result = lieuService.update(1L, updatedLieu);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumLieu());
        assertEquals("Nouvelles instructions", result.getInstructions());
        verify(lieuRepository, times(1)).save(updatedLieu);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        lieuService.deleteById(1L);

        // Assert
        verify(lieuRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenLieuExists_ShouldReturnTrue() {
        // Arrange
        when(lieuRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = lieuService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(lieuRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenLieuDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(lieuRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = lieuService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(lieuRepository, times(1)).existsById(999L);
    }
} 