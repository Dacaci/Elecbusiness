package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.repository.*;
import com.electricitybusiness.api.util.BorneUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
class BorneServiceTest {

    @Mock
    private BorneRepository borneRepository;
    
    @Mock
    private LieuRepository lieuRepository;
    
    @Mock
    private AdresseRepository adresseRepository;
    
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private BorneService borneService;

    private Borne borne1;
    private Borne borne2;
    private Lieu lieu;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        lieu = new Lieu();
        lieu.setNumLieu(1L);
        lieu.setInstructions("Parking principal");

        adresse = new Adresse();
        adresse.setNumAdresse(1L);
        adresse.setVille("Paris");
        adresse.setLieu(lieu);

        borne1 = new Borne();
        borne1.setNumBorne(1L);
        borne1.setNomBorne("Borne-A001");
        borne1.setLatitude(new BigDecimal("48.8566"));
        borne1.setLongitude(new BigDecimal("2.3522"));
        borne1.setPuissance(new BigDecimal("22.0"));
        borne1.setEtat(EtatBorne.ACTIVE);
        borne1.setOccupee(false);
        borne1.setSurPied(true);
        borne1.setLieu(lieu);

        borne2 = new Borne();
        borne2.setNumBorne(2L);
        borne2.setNomBorne("Borne-A002");
        borne2.setLatitude(new BigDecimal("48.8567"));
        borne2.setLongitude(new BigDecimal("2.3523"));
        borne2.setPuissance(new BigDecimal("50.0"));
        borne2.setEtat(EtatBorne.ACTIVE);
        borne2.setOccupee(false);
        borne2.setSurPied(true);
        borne2.setLieu(lieu);
    }

    @Test
    void findAll_ShouldReturnAllBornes() {
        // Arrange
        when(borneRepository.findAll()).thenReturn(Arrays.asList(borne1, borne2));

        // Act
        List<Borne> result = borneService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(borneRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenBorneExists_ShouldReturnBorne() {
        // Arrange
        when(borneRepository.findById(1L)).thenReturn(Optional.of(borne1));

        // Act
        Optional<Borne> result = borneService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(borne1.getNumBorne(), result.get().getNumBorne());
        assertEquals(borne1.getNomBorne(), result.get().getNomBorne());
        verify(borneRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenBorneDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(borneRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Borne> result = borneService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(borneRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedBorne() {
        // Arrange
        when(borneRepository.save(any(Borne.class))).thenReturn(borne1);

        // Act
        Borne result = borneService.save(borne1);

        // Assert
        assertNotNull(result);
        assertEquals(borne1.getNumBorne(), result.getNumBorne());
        assertEquals(borne1.getNomBorne(), result.getNomBorne());
        verify(borneRepository, times(1)).save(borne1);
    }

    @Test
    void update_WhenBorneExists_ShouldReturnUpdatedBorne() {
        // Arrange
        Borne updatedBorne = new Borne();
        updatedBorne.setNumBorne(1L);
        updatedBorne.setNomBorne("Borne-A001-Updated");
        updatedBorne.setLatitude(new BigDecimal("48.8566"));
        updatedBorne.setLongitude(new BigDecimal("2.3522"));
        updatedBorne.setPuissance(new BigDecimal("50.0"));
        updatedBorne.setEtat(EtatBorne.ACTIVE);
        updatedBorne.setOccupee(false);
        updatedBorne.setSurPied(true);
        updatedBorne.setLieu(lieu);
        
        when(borneRepository.save(any(Borne.class))).thenReturn(updatedBorne);

        // Act
        Borne result = borneService.update(1L, updatedBorne);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumBorne());
        assertEquals("Borne-A001-Updated", result.getNomBorne());
        verify(borneRepository, times(1)).save(updatedBorne);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        borneService.deleteById(1L);

        // Assert
        verify(borneRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenBorneExists_ShouldReturnTrue() {
        // Arrange
        when(borneRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = borneService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(borneRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenBorneDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(borneRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = borneService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(borneRepository, times(1)).existsById(999L);
    }

    @Test
    void findNearbyBornes_ShouldReturnBornesInRadius() {
        // Arrange
        List<Borne> allBornes = Arrays.asList(borne1, borne2);
        when(borneRepository.findAll()).thenReturn(allBornes);

        try (MockedStatic<BorneUtils> borneUtils = mockStatic(BorneUtils.class)) {
            borneUtils.when(() -> BorneUtils.getNearbyBornes(anyList(), anyDouble(), anyDouble(), anyDouble()))
                    .thenReturn(Arrays.asList(borne1));

            // Act
            List<Borne> result = borneService.findNearbyBornes(2.3522, 48.8566, 1000.0);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(borne1.getNumBorne(), result.get(0).getNumBorne());
            verify(borneRepository, times(1)).findAll();
        }
    }

    @Test
    void findAvailableBornesInCityAtDate_ShouldReturnAvailableBornes() {
        // Arrange
        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plusHours(2);
        
        when(adresseRepository.findByVille("Paris")).thenReturn(Arrays.asList(adresse));
        when(borneRepository.findByLieu(lieu)).thenReturn(Arrays.asList(borne1));

        // Act
        List<Borne> result = borneService.findAvailableBornesInCityAtDate("Paris", dateDebut, dateFin);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(borne1.getNumBorne(), result.get(0).getNumBorne());
        verify(adresseRepository, times(1)).findByVille("Paris");
        verify(borneRepository, times(1)).findByLieu(lieu);
    }
} 