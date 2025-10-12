package com.example.msevent;

import com.example.msevent.dto.response.SpeakerResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Speaker;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.SpeakerRepository;
import com.example.msevent.service.SpeakerService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpeakerServiceTest {

    private final SpeakerRepository speakerRepository = mock(SpeakerRepository.class);
    private final EventMapper mapper = mock(EventMapper.class);
    private final SpeakerService speakerService = new SpeakerService(speakerRepository, mapper);

    @Test
    void create_shouldReturnSpeakerResponse() {
        Speaker speaker = new Speaker();
        speaker.setFullName("John Doe");

        Speaker savedSpeaker = new Speaker();
        savedSpeaker.setId(1L);
        savedSpeaker.setFullName("John Doe");

        when(speakerRepository.save(speaker)).thenReturn(savedSpeaker);
        when(mapper.toDto(savedSpeaker)).thenReturn(new SpeakerResponse());

        SpeakerResponse response = speakerService.create(speaker);

        assertNotNull(response);
        verify(speakerRepository, times(1)).save(speaker);
        verify(mapper, times(1)).toDto(savedSpeaker);
    }

    @Test
    void update_existingSpeaker_shouldReturnSpeakerResponse() {
        Speaker speaker = new Speaker();
        speaker.setId(1L);
        Speaker updated = new Speaker();
        updated.setFullName("Jane Doe");

        when(speakerRepository.findById(1L)).thenReturn(Optional.of(speaker));
        when(speakerRepository.save(speaker)).thenReturn(speaker);
        when(mapper.toDto(speaker)).thenReturn(new SpeakerResponse());

        SpeakerResponse response = speakerService.update(1L, updated);

        assertNotNull(response);
        verify(speakerRepository, times(1)).findById(1L);
        verify(speakerRepository, times(1)).save(speaker);
        verify(mapper, times(1)).toDto(speaker);
    }

    @Test
    void update_nonExistingSpeaker_shouldThrowException() {
        Speaker updated = new Speaker();
        when(speakerRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> speakerService.update(1L, updated));

        assertEquals("Speaker not found with id: 1", ex.getMessage());
    }

    @Test
    void getById_existingSpeaker_shouldReturnDto() {
        Speaker speaker = new Speaker();
        speaker.setId(1L);

        when(speakerRepository.findById(1L)).thenReturn(Optional.of(speaker));
        when(mapper.toDto(speaker)).thenReturn(new SpeakerResponse());

        SpeakerResponse response = speakerService.getById(1L);

        assertNotNull(response);
        verify(speakerRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(speaker);
    }

    @Test
    void getById_nonExistingSpeaker_shouldThrowException() {
        when(speakerRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> speakerService.getById(1L));

        assertEquals("Speaker not found with id: 1", ex.getMessage());
    }

    @Test
    void listAll_shouldReturnListOfDtos() {
        Speaker speaker = new Speaker();
        when(speakerRepository.findAll()).thenReturn(List.of(speaker));
        when(mapper.toDto(speaker)).thenReturn(new SpeakerResponse());

        List<SpeakerResponse> responseList = speakerService.listAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(speakerRepository, times(1)).findAll();
        verify(mapper, times(1)).toDto(speaker);
    }

    @Test
    void delete_existingSpeaker_shouldCallRepository() {
        when(speakerRepository.existsById(1L)).thenReturn(true);

        speakerService.delete(1L);

        verify(speakerRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingSpeaker_shouldThrowException() {
        when(speakerRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> speakerService.delete(1L));

        assertEquals("Speaker not found with id: 1", ex.getMessage());
    }
}
