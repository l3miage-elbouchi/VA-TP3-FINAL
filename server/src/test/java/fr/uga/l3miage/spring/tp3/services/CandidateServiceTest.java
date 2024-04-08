package fr.uga.l3miage.spring.tp3.services;

import static org.mockito.Mockito.*;
        import static org.mockito.BDDMockito.*;
        import static org.junit.jupiter.api.Assertions.*;

        import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class CandidateServiceTest {

    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;

    @BeforeEach
    void setUp() {
        // Setup for each test can go here if needed
    }

    @Test
    @DisplayName("Calcul de la moyenne des notes d'un candidat trouvé")
    void whenCandidateFound_thenCalculateAverage1() throws CandidateNotFoundException {
        // Given
        CandidateEntity candidateEntity = new CandidateEntity();
        candidateEntity.setId(1L);

        Set<CandidateEvaluationGridEntity> evaluations = new HashSet<>();
        // Supprimer la deuxième déclaration de la variable candidateEntity
        evaluations.add(new CandidateEvaluationGridEntity());

        candidateEntity.setCandidateEvaluationGridEntities(evaluations);

        given(candidateComponent.getCandidatById(1L)).willReturn(candidateEntity);

        // When
        double average = candidateService.getCandidateAverage(1L);

        // Then
        assertEquals(15.0, average);
    }

    @Test
    @DisplayName("Calcul de la moyenne des notes d'un candidat trouvé")
    void whenCandidateFound_thenCalculateAverage2() throws CandidateNotFoundException {
        // Given
        CandidateEntity candidateEntity = new CandidateEntity();
        candidateEntity.setId(1L);

        Set<CandidateEvaluationGridEntity> evaluations = new HashSet<>();
        evaluations.add(new CandidateEvaluationGridEntity());

        candidateEntity.setCandidateEvaluationGridEntities(evaluations);

        given(candidateComponent.getCandidatById(1L)).willReturn(candidateEntity);

        // When
        double average = candidateService.getCandidateAverage(1L);

        // Then
        assertEquals(15.0, average);
    }


    @Test
    @DisplayName("Changer l'état d'une session de EVAL_STARTED à EVAL_ENDED")
    void whenUpdatingSessionStatus_thenCheckTransitionIsValid() {
        // Vous devrez simuler les étapes nécessaires pour mettre à jour le statut d'une session
        // et vérifier qu'elle passe correctement de EVAL_STARTED à EVAL_ENDED
    }
}