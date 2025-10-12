package com.example.msevent.controller;

import com.example.msevent.dto.request.EventRequest;
import com.example.msevent.dto.response.EventResponse;
import com.example.msevent.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> create(@Validated @RequestBody EventRequest request) {
        log.info("API Call: Create Event");
        return ResponseEntity.ok(eventService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> update(@PathVariable Long id,
                                                @Validated @RequestBody EventRequest request) {
        log.info("API Call: Update Event with id {}", id);
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("API Call: Delete Event with id {}", id);
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<EventResponse> getById(@PathVariable Long id) {
        log.info("API Call: Get Event by id {}", id);
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'USER')")
    public ResponseEntity<List<EventResponse>> listAll(@RequestParam(required = false) String category) {
        log.info("API Call: List Events");
        if (category != null) {
            return ResponseEntity.ok(eventService.listByCategory(category));
        }
        return ResponseEntity.ok(eventService.listAll());
    }
}
