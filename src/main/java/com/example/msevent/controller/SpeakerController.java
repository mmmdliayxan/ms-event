package com.example.msevent.controller;

import com.example.msevent.dto.response.SpeakerResponse;
import com.example.msevent.model.Speaker;
import com.example.msevent.service.SpeakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
@Slf4j
public class SpeakerController {

    private final SpeakerService speakerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<SpeakerResponse> create(@RequestBody Speaker speaker) {
        log.info("API Call: Create Speaker");
        return ResponseEntity.ok(speakerService.create(speaker));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<SpeakerResponse> update(@PathVariable Long id, @RequestBody Speaker speaker) {
        log.info("API Call: Update Speaker with id {}", id);
        return ResponseEntity.ok(speakerService.update(id, speaker));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("API Call: Delete Speaker with id {}", id);
        speakerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<SpeakerResponse> getById(@PathVariable Long id) {
        log.info("API Call: Get Speaker by id {}", id);
        return ResponseEntity.ok(speakerService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<List<SpeakerResponse>> listAll() {
        log.info("API Call: List all speakers");
        return ResponseEntity.ok(speakerService.listAll());
    }
}
