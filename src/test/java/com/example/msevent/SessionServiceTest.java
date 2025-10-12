package com.example.msevent;

import com.example.msevent.dto.response.SessionResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Session;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.SessionRepository;
import com.example.msevent.service.SessionService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private final SessionRepository sessionRepository = mock(SessionRepository.class);
    private final EventMapper mapper = mock(EventMapper.class);
    private final SessionService sessionService = new SessionService(sessionRepository, mapper);

    @Test
    void getById_existingSession_shouldReturnDto() {
        Session session = new Session();
        session.setId(1L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(mapper.toDto(session)).thenReturn(new SessionResponse());

        SessionResponse response = sessionService.getById(1L);

        assertNotNull(response);
        verify(sessionRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(session);
    }

    @Test
    void getById_nonExistingSession_shouldThrowException() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> sessionService.getById(1L));

        assertEquals("Session not found with id: 1", ex.getMessage());
    }

    @Test
    void listAll_shouldReturnListOfDtos() {
        Session session = new Session();
        when(sessionRepository.findAll()).thenReturn(List.of(session));
        when(mapper.toDto(session)).thenReturn(new SessionResponse());

        List<SessionResponse> responseList = sessionService.listAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(sessionRepository, times(1)).findAll();
        verify(mapper, times(1)).toDto(session);
    }

    @Test
    void delete_existingSession_shouldCallRepository() {
        when(sessionRepository.existsById(1L)).thenReturn(true);

        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingSession_shouldThrowException() {
        when(sessionRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> sessionService.delete(1L));

        assertEquals("Session not found with id: 1", ex.getMessage());
    }
}
