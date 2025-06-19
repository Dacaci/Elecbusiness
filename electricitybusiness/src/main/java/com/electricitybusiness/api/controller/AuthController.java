package com.electricitybusiness.api.controller;

import com.electricitybusiness.api.dto.AuthenticationRequest;
import com.electricitybusiness.api.dto.AuthenticationResponse;
import com.electricitybusiness.api.dto.RegisterRequest;
import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import com.electricitybusiness.api.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        var user = new Utilisateur();
        user.setAdresseMail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        user.setPseudo(request.getPseudo());
        user.setNomUtilisateur(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setRole(RoleUtilisateur.CLIENT);
        user.setDateDeNaissance(request.getDateDeNaissance());
        user.setBanni(false);
        user.setCompteValide(true);

        utilisateurRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        var user = utilisateurRepository.findByAdresseMail(request.getEmail())
            .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
} 