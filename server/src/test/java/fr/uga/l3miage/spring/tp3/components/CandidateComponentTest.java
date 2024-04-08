package fr.uga.l3miage.spring.tp3.components;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;
import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase
public class CandidateComponentTest {

    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    private CandidateEntity testCandidate;

    @BeforeEach
    void setUp() {
        // Configuration initiale pour chaque test
        testCandidate = CandidateEntity.builder()
                .id(1L)
                .email("test@example.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Échec de la récupération d'un candidat par ID - Non trouvé")
    void getCandidateByIdNotFound() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> candidateComponent.getCandidatById(1L));
    }

    @Test
    @DisplayName("Récupération réussie d'un candidat par ID")
    void getCandidateByIdFound() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));

        assertDoesNotThrow(() -> {
            CandidateEntity foundCandidate = candidateComponent.getCandidatById(1L);
            assertNotNull(foundCandidate);
            assertEquals(1L, foundCandidate.getId());
            assertEquals("test@example.com", foundCandidate.getEmail());
        });
    }

    // Ajout d'un nouveau test pour vérifier la récupération de tous les candidats
    @Test
    @DisplayName("Récupération de tous les candidats")
    void getAllCandidates() {
        HashSet<CandidateEntity> candidateSet = new HashSet<>();
        candidateSet.add(testCandidate);
        when(candidateRepository.findAll()).thenReturn(new ArrayList<>(candidateSet));

        Set<CandidateEntity> candidates = candidateComponent.getAllCandidates();

        assertNotNull(candidates);
        assertFalse(candidates.isEmpty());
        assertTrue(candidates.contains(testCandidate));
    }
}