package com.electricitybusiness.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Recharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private Double montant;
    private LocalDateTime dateRecharge;
    private String reference;
    private String statut;
} 