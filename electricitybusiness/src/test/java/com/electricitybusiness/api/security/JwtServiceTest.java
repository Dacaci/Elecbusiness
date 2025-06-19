package com.electricitybusiness.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = new User("test@example.com", "password", new ArrayList<>());
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void extractUsername_ShouldExtractCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = new User("different@example.com", "password", new ArrayList<>());
        assertFalse(jwtService.isTokenValid(token, differentUser));
    }
} 