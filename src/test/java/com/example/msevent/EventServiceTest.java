package com.example.msevent;

import com.example.msevent.client.UserClient;
import com.example.msevent.dto.request.EventRequest;
import com.example.msevent.dto.request.UserResponse;
import com.example.msevent.dto.response.EventResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Category;
import com.example.msevent.model.Event;
import com.example.msevent.model.Role;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.EventRepository;
import com.example.msevent.repository.SpeakerRepository;
import com.example.msevent.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private final EventRepository eventRepository = mock(EventRepository.class);
    private final SpeakerRepository speakerRepository = mock(SpeakerRepository.class);
    private final EventMapper mapper = mock(EventMapper.class);
    private final UserClient userClient = mock(UserClient.class);
    private final EventService eventService = new EventService(eventRepository, speakerRepository, mapper, userClient);

    private final Long userId = 42L; // test üçün

    @Test
    void create_shouldReturnEventResponse() {
        Long userId = 5L; // əgər yuxarıda yoxdur, əlavə et

        EventRequest request = new EventRequest();
        request.setTitle("Test Event");
        request.setCategory(Category.CONFERENCE);
        request.setStartAt(OffsetDateTime.now());
        request.setEndAt(OffsetDateTime.now().plusHours(2));

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");

        UserResponse userDto = new UserResponse();
        userDto.setUsername("testuser");
        userDto.setRole(Role.ORGANIZER);

        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(1L);
        eventResponse.setTitle("Test Event");

        when(mapper.toEntity(request)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(mapper.toDto(event)).thenReturn(eventResponse);

        // ✅ Feign Client ResponseEntity mock
        when(userClient.getUserById(userId)).thenReturn(ResponseEntity.ok(userDto));

        EventResponse response = eventService.create(request, userId);

        assertNotNull(response);
        verify(eventRepository, times(1)).save(event);
        verify(mapper, times(1)).toDto(event);
        verify(userClient, times(1)).getUserById(userId);
    }

    @Test
    void update_existingEvent_shouldReturnEventResponse() {
        EventRequest request = new EventRequest();
        Event event = new Event();
        event.setId(1L);
        event.setCreatedByUserId(userId);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(mapper.toDto(event)).thenReturn(new EventResponse());
        when(eventRepository.save(event)).thenReturn(event);

        EventResponse response = eventService.update(1L, request, userId);

        assertNotNull(response);
        verify(eventRepository, times(1)).save(event);
        verify(mapper, times(1)).updateFromDto(request, event);
    }

    @Test
    void update_nonExistingEvent_shouldThrowException() {
        EventRequest request = new EventRequest();

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> eventService.update(1L, request, userId));

        assertEquals("Event not found with id: 1", ex.getMessage());
    }

    @Test
    void update_eventNotOwnedByUser_shouldThrowSecurityException() {
        EventRequest request = new EventRequest();
        Event event = new Event();
        event.setId(1L);
        event.setCreatedByUserId(999L); // fərqli user

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> eventService.update(1L, request, userId));

        assertEquals("You are not allowed to update this event!", ex.getMessage());
    }

    @Test
    void delete_existingEvent_shouldCallRepository() {
        Event event = new Event();
        event.setId(1L);
        event.setCreatedByUserId(userId);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.delete(1L, userId);

        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void delete_nonExistingEvent_shouldThrowException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> eventService.delete(1L, userId));

        assertEquals("Event not found with id: 1", ex.getMessage());
    }

    @Test
    void delete_eventNotOwnedByUser_shouldThrowSecurityException() {
        Event event = new Event();
        event.setId(1L);
        event.setCreatedByUserId(999L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> eventService.delete(1L, userId));

        assertEquals("You are not allowed to delete this event!", ex.getMessage());
    }

    @Test
    void getById_existingEvent_shouldReturnEventResponse() {
        Event event = new Event();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(mapper.toDto(event)).thenReturn(new EventResponse());

        EventResponse response = eventService.getById(1L);

        assertNotNull(response);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getById_nonExistingEvent_shouldThrowException() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> eventService.getById(1L));

        assertEquals("Event not found with id: 1", ex.getMessage());
    }

    @Test
    void listAll_shouldReturnList() {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(List.of(event));
        when(mapper.toDto(event)).thenReturn(new EventResponse());

        var responseList = eventService.listAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void listByCategory_shouldReturnList() {
        Event event = new Event();
        when(eventRepository.findByCategory(Category.CONFERENCE)).thenReturn(List.of(event));
        when(mapper.toDto(event)).thenReturn(new EventResponse());

        var responseList = eventService.listByCategory("CONFERENCE");

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(eventRepository, times(1)).findByCategory(Category.CONFERENCE);
    }
}
