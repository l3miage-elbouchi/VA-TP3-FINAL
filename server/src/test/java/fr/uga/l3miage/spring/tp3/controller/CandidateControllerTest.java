
package fr.uga.l3miage.spring.tp3.controller;

import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.services.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    private CandidateEntity sampleCandidate;

    @BeforeEach
    void setUp() {
        sampleCandidate = new CandidateEntity();
        sampleCandidate.setId(1L);
        sampleCandidate.setEmail("candidate@example.com");
        // Autres initialisations si nécessaire
    }

    @Test
    @DisplayName("Récupérer la moyenne d'un candidat par son ID - Succès")
    void getCandidateAverageFound() throws Exception {
        given(candidateService.getCandidateAverage(1L)).willReturn(75.0);

        mockMvc.perform(get("/api/candidates/{id}/average", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.average").value(75.0));
    }

    @Test
    @DisplayName("Récupérer la moyenne d'un candidat par son ID - Candidat non trouvé")
    void getCandidateAverageNotFound() throws Exception {
        willThrow(new CandidateNotFoundRestException("Candidate not found", 2L)).given(candidateService).getCandidateAverage(2L);

        mockMvc.perform(get("/api/candidates/{id}/average", 2L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Candidate not found"));
    }

    @Test
    @DisplayName("Création d'une session d'examen - Succès")
    void createExamSessionSuccess() throws Exception {
        // Mock the successful creation of an exam session
        // Suppose createExamSession is a POST endpoint that takes a session creation request and returns the created session

        String sessionRequestJson = "{\"name\": \"Session 1\", \"description\": \"Description of Session 1\"}";

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Session 1"));
        // Note: Make sure your controller is set up to return the created session object for this test to pass
    }

    // Ajouter d'autres tests pour couvrir plus de scénarios et cas d'usage...
}