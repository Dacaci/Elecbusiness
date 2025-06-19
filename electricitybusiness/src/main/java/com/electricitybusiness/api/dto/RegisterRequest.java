package com.electricitybusiness.api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String pseudo;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
} 