package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SessionRepositoryTest {

    @Autowired
    private EcosSessionRepository sessionRepository;

    private EcosSessionEntity newSession;

    @BeforeEach
    void setUp() {
        // Préparation d'une session de test
        newSession = new EcosSessionEntity();
        newSession.setName("Final Exam");
        newSession.setStartDate(LocalDateTime.now());
        newSession.setEndDate(LocalDateTime.now().plusDays(1));
        newSession.setStatus(SessionStatus.CREATED);
    }

    @Test
    @DisplayName("Create and Retrieve Session")
    void createAndRetrieveSession() {
        // Enregistrement de la session
        EcosSessionEntity savedSession = sessionRepository.save(newSession);

        // Vérification que la session a bien été enregistrée
        assertNotNull(savedSession.getId());

        // Récupération de la session
        EcosSessionEntity foundSession = sessionRepository.findById(savedSession.getId()).orElse(null);

        // Vérifications
        assertNotNull(foundSession);
        assertEquals("Final Exam", foundSession.getName());
        assertEquals(SessionStatus.CREATED, foundSession.getStatus());
    }

    @Test
    @DisplayName("Update Session Status")
    void updateSessionStatus() {
        // Enregistrement initial de la session
        EcosSessionEntity savedSession = sessionRepository.save(newSession);

        // Mise à jour du statut
        savedSession.setStatus(SessionStatus.VALIDATED);
        sessionRepository.save(savedSession);

        // Récupération de la session mise à jour
        EcosSessionEntity updatedSession = sessionRepository.findById(savedSession.getId()).orElse(null);

        // Vérification du nouveau statut
        assertEquals(SessionStatus.VALIDATED, updatedSession.getStatus());
    }

    @Test
    @DisplayName("Find Sessions By Status")
    void findSessionsByStatus() {
        // Enregistrement de quelques sessions
        sessionRepository.save(newSession);
        EcosSessionEntity anotherSession = new EcosSessionEntity();
        anotherSession.setName("Midterm Exam");
        anotherSession.setStartDate(LocalDateTime.now().plusDays(2));
        anotherSession.setEndDate(LocalDateTime.now().plusDays(3));
        anotherSession.setStatus(SessionStatus.VALIDATED);
        sessionRepository.save(anotherSession);

        // Recherche des sessions par statut
        List<EcosSessionEntity> createdSessions = sessionRepository.findAllByStatus(SessionStatus.CREATED);
        List<EcosSessionEntity> inProgressSessions = sessionRepository.findAllByStatus(SessionStatus.VALIDATED);

        // Vérifications
        assertFalse(createdSessions.isEmpty());
        assertFalse(inProgressSessions.isEmpty());
        assertEquals(1, createdSessions.size());
        assertEquals(1, inProgressSessions.size());
        assertEquals("Final Exam", createdSessions.get(0).getName());
        assertEquals("Midterm Exam", inProgressSessions.get(0).getName());
    }

    @Test
    @DisplayName("Session Save With Duplicate Name Throws Exception")
    void saveSessionWithDuplicateNameThrowsException() {
        // Tentative d'enregistrement de deux sessions avec le même nom
        sessionRepository.save(newSession);

        EcosSessionEntity duplicateNameSession = new EcosSessionEntity();
        duplicateNameSession.setName(newSession.getName()); // Même nom
        duplicateNameSession.setStartDate(LocalDateTime.now().plusDays(5));
        duplicateNameSession.setEndDate(LocalDateTime.now().plusDays(6));
        duplicateNameSession.setStatus(SessionStatus.PLANNED);

        assertThrows(DataIntegrityViolationException.class, () -> sessionRepository.save(duplicateNameSession),
                "Should throw exception due to duplicate sessionname");
}
}