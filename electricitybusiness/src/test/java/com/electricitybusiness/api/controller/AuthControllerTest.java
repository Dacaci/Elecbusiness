package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.AuthenticationRequest;
import com.electricitybusiness.api.dto.RegisterRequest;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        utilisateurRepository.deleteAll();
    }

    @Test
    void register_ShouldCreateNewUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setPseudo("testuser");
        request.setNom("Test");
        request.setPrenom("User");
        request.setDateDeNaissance(LocalDate.of(2000, 1, 1));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        Utilisateur savedUser = utilisateurRepository.findByAdresseMail("test@example.com").orElse(null);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getAdresseMail());
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        // Créer un utilisateur de test
        Utilisateur user = new Utilisateur();
        user.setAdresseMail("test@example.com");
        user.setMotDePasse(passwordEncoder.encode("password123"));
        user.setPseudo("testuser");
        user.setNomUtilisateur("Test");
        user.setPrenom("User");
        utilisateurRepository.save(user);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
} 