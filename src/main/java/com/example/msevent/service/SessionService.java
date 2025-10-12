package com.example.msevent.service;

import com.example.msevent.dto.response.SessionResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Session;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventMapper mapper;

    public SessionResponse getById(Long id) {
        log.info("Fetching session by id: {}", id);
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        return mapper.toDto(session);
    }

    public List<SessionResponse> listAll() {
        log.info("Fetching all sessions");
        return sessionRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        log.info("Deleting session with id: {}", id);
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
        log.info("Session deleted successfully: {}", id);
    }
}
