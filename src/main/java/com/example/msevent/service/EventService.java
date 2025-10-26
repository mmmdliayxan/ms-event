package com.example.msevent.service;

import com.example.msevent.client.UserClient;
import com.example.msevent.dto.request.EventRequest;
import com.example.msevent.dto.request.UserDto;
import com.example.msevent.dto.response.EventResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Category;
import com.example.msevent.model.Event;
import com.example.msevent.model.Session;
import com.example.msevent.model.Speaker;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.EventRepository;
import com.example.msevent.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final SpeakerRepository speakerRepository;
    private final EventMapper mapper;
    private final UserClient userClient; // Feign Client ilə user məlumatı üçün (optional)

    @Transactional
    public EventResponse create(EventRequest request, Long userId) {
        log.info("Creating new event by userId={}", userId);

        UserDto user = userClient.getUserById(userId);
        log.debug("User fetched from ms-auth: {} ({})", user.getUsername(), user.getRole());

        Event event = mapper.toEntity(request);
        event.setCreatedByUserId(userId);

        if (request.getSessions() != null) {
            List<Session> sessions = request.getSessions().stream().map(sessionReq -> {
                Session session = mapper.toEntity(sessionReq);
                session.setEvent(event);

                if (sessionReq.getSpeakerIds() != null) {
                    List<Speaker> speakers = speakerRepository.findAllById(sessionReq.getSpeakerIds());
                    session.setSpeakers(speakers);
                    log.debug("Added {} speakers to session {}", speakers.size(), session.getTitle());
                }
                return session;
            }).collect(Collectors.toList());

            event.setSessions(sessions);
        }

        Event saved = eventRepository.save(event);
        log.info("Event created with id={} by userId={}", saved.getId(), userId);
        return mapper.toDto(saved);
    }

    @Transactional
    public EventResponse update(Long id, EventRequest request, Long userId) {
        log.info("Updating event id={} by userId={}", id, userId);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        if (!event.getCreatedByUserId().equals(userId)) {
            throw new SecurityException("You are not allowed to update this event!");
        }

        mapper.updateFromDto(request, event);

        if (request.getSessions() != null) {
            event.getSessions().clear();

            List<Session> sessions = request.getSessions().stream().map(sessionReq -> {
                Session session = mapper.toEntity(sessionReq);
                session.setEvent(event);

                if (sessionReq.getSpeakerIds() != null) {
                    List<Speaker> speakers = speakerRepository.findAllById(sessionReq.getSpeakerIds());
                    session.setSpeakers(speakers);
                    log.debug("Updated {} speakers in session {}", speakers.size(), session.getTitle());
                }
                return session;
            }).collect(Collectors.toList());

            event.setSessions(sessions);
        }

        Event saved = eventRepository.save(event);
        log.info("Event updated successfully id={} by userId={}", saved.getId(), userId);
        return mapper.toDto(saved);
    }

    public void delete(Long id, Long userId) {
        log.info("Deleting event id={} by userId={}", id, userId);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        if (!event.getCreatedByUserId().equals(userId)) {
            throw new SecurityException("You are not allowed to delete this event!");
        }

        eventRepository.delete(event);
        log.info("Event deleted successfully by userId={}", userId);
    }

    public EventResponse getById(Long id) {
        log.info("Fetching event by id={}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapper.toDto(event);
    }

    public List<EventResponse> listAll() {
        log.info("Fetching all events");
        return eventRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventResponse> listByCategory(String category) {
        log.info("Fetching events by category={}", category);
        return eventRepository.findByCategory(Category.valueOf(category.toUpperCase()))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getByOrganizer(Long userId) {
        List<Event> events = eventRepository.findByCreatedByUserId(userId);
        return events.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}