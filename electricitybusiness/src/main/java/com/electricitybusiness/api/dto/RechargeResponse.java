package com.electricitybusiness.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RechargeResponse {
    private boolean success;
    private String message;
    private String numero;
    private Double montant;
    private LocalDateTime dateRecharge;
    private String reference;
} 