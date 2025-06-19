package com.electricitybusiness.api.service;

import com.electricitybusiness.api.model.*;
import com.electricitybusiness.api.repository.TarifHoraireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarifHoraireServiceTest {

    @Mock
    private TarifHoraireRepository tarifHoraireRepository;

    @InjectMocks
    private TarifHoraireService tarifHoraireService;

    private TarifHoraire tarif1;
    private TarifHoraire tarif2;
    private Borne borne;

    @BeforeEach
    void setUp() {
        borne = new Borne();
        borne.setNumBorne(1L);
        borne.setNomBorne("Borne-A001");
        borne.setPuissance(new BigDecimal("22.0"));

        tarif1 = new TarifHoraire();
        tarif1.setNumTarif(1L);
        tarif1.setHeureDebut(LocalTime.of(8, 0));
        tarif1.setHeureFin(LocalTime.of(18, 0));
        tarif1.setTarifParMinute(new BigDecimal("0.004167"));
        tarif1.setDateDebut(LocalDate.of(2025, 6, 1));
        tarif1.setActif(true);
        tarif1.setBorne(borne);

        tarif2 = new TarifHoraire();
        tarif2.setNumTarif(2L);
        tarif2.setHeureDebut(LocalTime.of(18, 0));
        tarif2.setHeureFin(LocalTime.of(8, 0));
        tarif2.setTarifParMinute(new BigDecimal("0.003333"));
        tarif2.setDateDebut(LocalDate.of(2025, 6, 1));
        tarif2.setActif(true);
        tarif2.setBorne(borne);
    }

    @Test
    void findAll_ShouldReturnAllTarifs() {
        // Arrange
        when(tarifHoraireRepository.findAll()).thenReturn(Arrays.asList(tarif1, tarif2));

        // Act
        List<TarifHoraire> result = tarifHoraireService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tarifHoraireRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenTarifExists_ShouldReturnTarif() {
        // Arrange
        when(tarifHoraireRepository.findById(1L)).thenReturn(Optional.of(tarif1));

        // Act
        Optional<TarifHoraire> result = tarifHoraireService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tarif1.getNumTarif(), result.get().getNumTarif());
        assertEquals(tarif1.getTarifParMinute(), result.get().getTarifParMinute());
        verify(tarifHoraireRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenTarifDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(tarifHoraireRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<TarifHoraire> result = tarifHoraireService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(tarifHoraireRepository, times(1)).findById(999L);
    }

    @Test
    void save_ShouldReturnSavedTarif() {
        // Arrange
        when(tarifHoraireRepository.save(any(TarifHoraire.class))).thenReturn(tarif1);

        // Act
        TarifHoraire result = tarifHoraireService.save(tarif1);

        // Assert
        assertNotNull(result);
        assertEquals(tarif1.getNumTarif(), result.getNumTarif());
        assertEquals(tarif1.getTarifParMinute(), result.getTarifParMinute());
        verify(tarifHoraireRepository, times(1)).save(tarif1);
    }

    @Test
    void update_WhenTarifExists_ShouldReturnUpdatedTarif() {
        // Arrange
        TarifHoraire updatedTarif = new TarifHoraire();
        updatedTarif.setNumTarif(1L);
        updatedTarif.setHeureDebut(LocalTime.of(8, 0));
        updatedTarif.setHeureFin(LocalTime.of(20, 0));
        updatedTarif.setTarifParMinute(new BigDecimal("0.005000"));
        updatedTarif.setDateDebut(LocalDate.of(2025, 6, 1));
        updatedTarif.setActif(true);
        updatedTarif.setBorne(borne);
        
        when(tarifHoraireRepository.save(any(TarifHoraire.class))).thenReturn(updatedTarif);

        // Act
        TarifHoraire result = tarifHoraireService.update(1L, updatedTarif);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getNumTarif());
        assertEquals(new BigDecimal("0.005000"), result.getTarifParMinute());
        verify(tarifHoraireRepository, times(1)).save(updatedTarif);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        tarifHoraireService.deleteById(1L);

        // Assert
        verify(tarifHoraireRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsById_WhenTarifExists_ShouldReturnTrue() {
        // Arrange
        when(tarifHoraireRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = tarifHoraireService.existsById(1L);

        // Assert
        assertTrue(result);
        verify(tarifHoraireRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_WhenTarifDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(tarifHoraireRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = tarifHoraireService.existsById(999L);

        // Assert
        assertFalse(result);
        verify(tarifHoraireRepository, times(1)).existsById(999L);
    }
} 