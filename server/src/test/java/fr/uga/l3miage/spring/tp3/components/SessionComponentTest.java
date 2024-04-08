package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionComponentTest {

    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    private EcosSessionEntity testSession;

    @BeforeEach
    void setUp() {
        // Initialisation avec une session de test
        testSession = new EcosSessionEntity();
        testSession.setId(1L);
        testSession.setName("Session 2023");
        testSession.setStatus(SessionStatus.CREATED);
        testSession.setStartDate(LocalDateTime.now());
        testSession.setEndDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("Création d'une session réussie")
    void createSession_Success() {
        // Given
        given(ecosSessionRepository.save(any(EcosSessionEntity.class))).willReturn(testSession);

        // When
        EcosSessionEntity createdSession = sessionComponent.createSession(testSession);

        // Then
        assertNotNull(createdSession);
        assertEquals(SessionStatus.CREATED, createdSession.getStatus());
        assertEquals("Session 2023", createdSession.getName());
    }

    @Test
    @DisplayName("Mise à jour du statut de la session - Succès")
    void updateSessionStatus_Success()  {
        // Given
        given(ecosSessionRepository.findById(testSession.getId())).willReturn(Optional.of(testSession));
        given(ecosSessionRepository.save(any(EcosSessionEntity.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        EcosSessionEntity updatedSession = sessionComponent.createSession(testSession);

        // Then
        assertNotNull(updatedSession);
        assertEquals(SessionStatus.CREATED, updatedSession.getStatus());
        assertEquals("Session 2023", updatedSession.getName());
    }
}
