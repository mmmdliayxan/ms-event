package com.example.msevent.controller;

import com.example.msevent.dto.response.SessionResponse;
import com.example.msevent.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<SessionResponse> getById(@PathVariable Long id) {
        log.info("API Call: Get Session by id {}", id);
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<List<SessionResponse>> listAll() {
        log.info("API Call: List all sessions");
        return ResponseEntity.ok(sessionService.listAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("API Call: Delete Session with id {}", id);
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
