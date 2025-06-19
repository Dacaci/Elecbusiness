package com.electricitybusiness.api;

import com.electricitybusiness.api.model.RoleUtilisateur;
import com.electricitybusiness.api.model.Utilisateur;
import com.electricitybusiness.api.model.Lieu;
import com.electricitybusiness.api.repository.UtilisateurRepository;
import com.electricitybusiness.api.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Pour s'assurer que les changements de la DB sont rollbackés après chaque test
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Nettoyage et création d'un utilisateur de test avant chaque test
        utilisateurRepository.deleteAll();

        // Créer un lieu de test
        Lieu testLieu = new Lieu();
        testLieu.setNumLieu(1L);
        testLieu.setInstructions("Instructions pour le lieu de test");
        entityManager.persist(testLieu); // Persister le lieu avant de l'utiliser

        Utilisateur user = new Utilisateur();
        user.setAdresseMail("user@example.com");
        user.setMotDePasse(passwordEncoder.encode("password"));
        user.setPseudo("testuser");
        user.setNomUtilisateur("Test");
        user.setPrenom("User");
        user.setRole(RoleUtilisateur.CLIENT);
        user.setDateDeNaissance(java.time.LocalDate.of(2000, 1, 1));
        user.setBanni(false);
        user.setCompteValide(true);
        user.setLieu(testLieu); // Assigner le lieu de test
        utilisateurRepository.save(user);

        Utilisateur admin = new Utilisateur();
        admin.setAdresseMail("admin@example.com");
        admin.setMotDePasse(passwordEncoder.encode("adminpass"));
        admin.setPseudo("adminuser");
        admin.setNomUtilisateur("Admin");
        admin.setPrenom("User");
        admin.setRole(RoleUtilisateur.ADMIN);
        admin.setDateDeNaissance(java.time.LocalDate.of(1990, 5, 10));
        admin.setBanni(false);
        admin.setCompteValide(true);
        admin.setLieu(testLieu); // Assigner le lieu de test
        utilisateurRepository.save(admin);
    }

    // Test d'accès à une route publique (login)
    @Test
    void loginEndpoint_shouldBePublic() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("user@example.com", "password"))))
                .andExpect(status().isOk());
    }

    // Test d'accès à une route protégée sans authentification
    @Test
    void protectedEndpoint_withoutAuth_shouldBeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/bornes")) // Exemple de route protégée
                .andExpect(status().isUnauthorized());
    }

    // Test d'accès à une route protégée avec authentification valide
    @Test
    void protectedEndpoint_withValidAuth_shouldBeOk() throws Exception {
        // Simuler un utilisateur authentifié pour le test
        mockMvc.perform(get("/api/bornes").with(user("user@example.com").password("password").roles("CLIENT")))
                .andExpect(status().isOk());
    }

    // Test d'accès à une route protégée avec un rôle insuffisant
    @Test
    void adminEndpoint_withUserRole_shouldBeForbidden() throws Exception {
        // Assumons que /api/admin est une route admin-only, à adapter si vous n'avez pas cette route
        mockMvc.perform(get("/api/utilisateurs").with(user("user@example.com").password("password").roles("CLIENT")))
                .andExpect(status().isForbidden()); // On s'attend à un 403 Forbidden
    }

    // Test d'accès à une route protégée avec un rôle suffisant (ADMIN)
    @Test
    void adminEndpoint_withAdminRole_shouldBeOk() throws Exception {
        mockMvc.perform(get("/api/utilisateurs").with(user("admin@example.com").password("adminpass").roles("ADMIN")))
                .andExpect(status().isOk()); // On s'attend à un 200 OK
    }
} 