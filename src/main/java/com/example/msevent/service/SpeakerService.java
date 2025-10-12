package com.example.msevent.service;

import com.example.msevent.dto.response.SpeakerResponse;
import com.example.msevent.mapper.EventMapper;
import com.example.msevent.model.Speaker;
import com.example.msevent.model.exception.ResourceNotFoundException;
import com.example.msevent.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeakerService {

    private final SpeakerRepository speakerRepository;
    private final EventMapper mapper;

    public SpeakerResponse getById(Long id) {
        log.info("Fetching speaker by id: {}", id);
        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker not found with id: " + id));
        return mapper.toDto(speaker);
    }

    public List<SpeakerResponse> listAll() {
        log.info("Fetching all speakers");
        return speakerRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        log.info("Deleting speaker with id: {}", id);
        if (!speakerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Speaker not found with id: " + id);
        }
        speakerRepository.deleteById(id);
        log.info("Speaker deleted successfully: {}", id);
    }

    public SpeakerResponse create(Speaker speaker) {
        log.info("Creating new speaker: {}", speaker.getFullName());
        Speaker saved = speakerRepository.save(speaker);
        log.info("Speaker created with id: {}", saved.getId());
        return mapper.toDto(saved);
    }

    public SpeakerResponse update(Long id, Speaker updatedSpeaker) {
        log.info("Updating speaker with id: {}", id);
        Speaker speaker = speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker not found with id: " + id));

        speaker.setFullName(updatedSpeaker.getFullName());
        speaker.setBio(updatedSpeaker.getBio());
        speaker.setCompany(updatedSpeaker.getCompany());
        speaker.setPosition(updatedSpeaker.getPosition());

        Speaker saved = speakerRepository.save(speaker);
        log.info("Speaker updated successfully: {}", saved.getId());
        return mapper.toDto(saved);
    }
}
