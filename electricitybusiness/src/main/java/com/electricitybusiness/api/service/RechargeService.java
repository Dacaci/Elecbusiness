package com.electricitybusiness.api.service;

import com.electricitybusiness.api.dto.RechargeRequest;
import com.electricitybusiness.api.dto.RechargeResponse;
import com.electricitybusiness.api.model.Recharge;
import com.electricitybusiness.api.repository.RechargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;

    @Transactional
    public RechargeResponse effectuerRecharge(RechargeRequest request) {
        // Validation du numéro et du montant
        if (request.getNumero() == null || request.getNumero().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone est requis");
        }
        if (request.getMontant() == null || request.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }

        // Création de la recharge
        Recharge recharge = new Recharge();
        recharge.setNumero(request.getNumero());
        recharge.setMontant(request.getMontant());
        recharge.setDateRecharge(LocalDateTime.now());
        recharge.setReference(UUID.randomUUID().toString());
        recharge.setStatut("SUCCESS");

        // Sauvegarde de la recharge
        recharge = rechargeRepository.save(recharge);

        // Création de la réponse
        RechargeResponse response = new RechargeResponse();
        response.setSuccess(true);
        response.setMessage("Recharge effectuée avec succès");
        response.setNumero(recharge.getNumero());
        response.setMontant(recharge.getMontant());
        response.setDateRecharge(recharge.getDateRecharge());
        response.setReference(recharge.getReference());

        return response;
    }

    public List<Recharge> getHistoriqueRecharges() {
        return rechargeRepository.findAll();
    }
} 