package com.electricitybusiness.api.dto;

import lombok.Data;

@Data
public class RechargeRequest {
    private String numero;
    private Double montant;
} 