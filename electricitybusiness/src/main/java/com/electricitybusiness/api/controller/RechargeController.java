package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.RechargeRequest;
import com.electricitybusiness.api.dto.RechargeResponse;
import com.electricitybusiness.api.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @PostMapping
    public ResponseEntity<RechargeResponse> effectuerRecharge(@RequestBody RechargeRequest request) {
        RechargeResponse response = rechargeService.effectuerRecharge(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historique")
    public ResponseEntity<?> getHistoriqueRecharges() {
        return ResponseEntity.ok(rechargeService.getHistoriqueRecharges());
    }
} 