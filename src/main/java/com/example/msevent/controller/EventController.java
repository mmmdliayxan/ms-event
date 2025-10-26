package com.example.msevent.controller;

import com.example.msevent.dto.request.EventRequest;
import com.example.msevent.dto.response.EventResponse;
import com.example.msevent.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request,
            @RequestHeader(value = "X-User-Id",required = false) Long userId) {

        log.info("Creating event by userId={}", userId);
        return ResponseEntity.ok(eventService.create(request, userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request,
            @RequestHeader(value = "X-User-Id",required = false) Long userId) {

        log.info("Updating event id={} by userId={}", id, userId);
        return ResponseEntity.ok(eventService.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id",required = false) Long userId) {

        eventService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','USER')")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','USER')")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.listAll());
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER','USER')")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(eventService.listByCategory(category));
    }

    @GetMapping("/my-events")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<List<EventResponse>> getEventsByOrganizer(@RequestHeader(value = "X-User-Id",required = false) Long userId) {
        log.info("API Call: Get Events by Organizer ID {}", userId);
        return ResponseEntity.ok(eventService.getByOrganizer(userId));
    }
}