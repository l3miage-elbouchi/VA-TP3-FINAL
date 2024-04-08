package fr.uga.l3miage.spring.tp3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    private EcosSessionEntity session;
    private fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus convertToResponseSessionStatus(fr.uga.l3miage.spring.tp3.enums.SessionStatus modelStatus) {
        switch (modelStatus) {
            case CREATED:
                return fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus.CREATED;
            // Complétez avec d'autres correspondances entre les statuts
            default:
                throw new IllegalArgumentException("Unmapped session status: " + modelStatus);
        }
    }
    private fr.uga.l3miage.spring.tp3.enums.SessionStatus convertToModelSessionStatus(fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus responseStatus) {
        switch (responseStatus) {
            case CREATED:
                return fr.uga.l3miage.spring.tp3.enums.SessionStatus.CREATED;
            // Ajoutez d'autres correspondances de statuts de session si nécessaire
            default:
                throw new IllegalArgumentException("Unmapped session status: " + responseStatus);
        }
    }
    @BeforeEach
    void setUp() {
        session = new EcosSessionEntity();
        session.setId(1L);
        session.setName("Sample Session");
        session.setStatus(SessionStatus.CREATED);
    }

    @Test
    @DisplayName("Create a Session - Success")
    void createSession_Success() throws Exception {
        SessionResponse sessionResponse = new SessionResponse();
        sessionResponse.setName("Sample Session");
        // Appliquez la conversion ici
        sessionResponse.setStatus(convertToResponseSessionStatus(SessionStatus.CREATED));

        given(sessionService.createSession(any())).willReturn(sessionResponse);

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Sample Session"))
                .andExpect(jsonPath("$.status").value(SessionStatus.CREATED.toString()));
    }

    @Test
    @DisplayName("Update Session Status - Success")
    void updateSessionStatus_Success() throws Exception {
        given(sessionService.updateSessionStatus(eq(session.getId()), eq(SessionStatus.VALIDATED))).willReturn(true);

        mockMvc.perform(put("/api/sessions/{id}/status", session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus.VALIDATED)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true));
    }
    @Test
    @DisplayName("Update Session Status - Session Not Found")
    void updateSessionStatus_NotFound() throws Exception {
        given(sessionService.updateSessionStatus(anyLong(), any())).willThrow(new RuntimeException("Session not found"));

        mockMvc.perform(put("/api/sessions/{id}/status", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SessionStatus.VALIDATED)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Session not found")));
    }
}