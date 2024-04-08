package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    private EcosSessionEntity testSession;

    @BeforeEach
    void setUp() {
        testSession = new EcosSessionEntity();
        testSession.setId(1L);
        testSession.setName("Sample Session");
        testSession.setStatus(SessionStatus.CREATED);
        testSession.setStartDate(LocalDateTime.now());
        testSession.setEndDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("Create Session Successfully")
    void createSession_Success() {
        // Given
        given(sessionComponent.createSession(any(EcosSessionEntity.class))).willReturn(testSession);

        // When
        EcosSessionEntity createdSession = sessionService.createSession(new EcosSessionEntity());

        // Then
        assertNotNull(createdSession);
        assertEquals(SessionStatus.CREATED, createdSession.getStatus(), "Session status should be CREATED");
        assertEquals("Sample Session", createdSession.getName(), "Session name should match");
    }

    @Test
    @DisplayName("Update Session Status Successfully")
    void updateSessionStatus_Success()  {
        // Given
        ArgumentCaptor<Long> sessionIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<SessionStatus> sessionStatusCaptor = ArgumentCaptor.forClass(SessionStatus.class);
        willDoNothing().given(sessionComponent).updateSessionStatus(sessionIdCaptor.capture(), sessionStatusCaptor.capture());

        // When
        sessionService.updateSessionStatus(testSession.getId(), SessionStatus.VALIDATED);

        // Then
        assertEquals(testSession.getId(), sessionIdCaptor.getValue(), "Captured session ID should match the test session ID");
        assertEquals(SessionStatus.VALIDATED, sessionStatusCaptor.getValue(), "Session status should be updated to VALIDATED");
    }

}
