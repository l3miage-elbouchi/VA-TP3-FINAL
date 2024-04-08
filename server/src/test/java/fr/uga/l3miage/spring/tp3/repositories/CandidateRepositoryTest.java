package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Set; // Importez Set au lieu de List

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestCenterEntity.class)
public class CandidateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CandidateRepository candidateRepository;

    private TestCenterEntity testCenterEntity;

    @BeforeEach
    void setUp() {
        testCenterEntity = new TestCenterEntity();
        testCenterEntity.setCode(TestCenterCode.GRE);
        testCenterEntity.setUniversity("University of Grenoble Alpes");
        testCenterEntity = entityManager.persistAndFlush(testCenterEntity);
    }

    @Test
    @DisplayName("Trouver des candidats par code de centre de test")
    void whenFindByTestCenterCode_thenReturnCandidates() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setFirstname("John");
        candidate.setLastname("Doe");
        candidate.setEmail("john.doe@example.com");
        candidate.setBirthDate(LocalDate.of(1990, 1, 1));
        candidate.setTestCenterEntity(testCenterEntity);

        entityManager.persistAndFlush(candidate);

        Set<CandidateEntity> foundCandidates = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE); // Utilisez Set au lieu de List
        assertEquals(1, foundCandidates.size(), "Doit trouver un candidat lié au centre de test GRE.");
        assertEquals("John", foundCandidates.iterator().next().getFirstname(), "Le prénom du candidat trouvé doit être John.");
    }

    @Test
    @DisplayName("Ne pas trouver de candidats pour un code de centre de test inexistant")
    void whenFindByNonexistentTestCenterCode_thenReturnEmpty() {
        Set<CandidateEntity> foundCandidates = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.PAR); // Utilisez Set au lieu de List
        assertTrue(foundCandidates.isEmpty(), "Ne doit trouver aucun candidat pour un centre de test inexistant.");
    }

}
