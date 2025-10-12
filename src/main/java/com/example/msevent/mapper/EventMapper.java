package com.example.msevent.mapper;

import com.example.msevent.dto.request.EventRequest;
import com.example.msevent.dto.request.SessionRequest;
import com.example.msevent.dto.response.EventResponse;
import com.example.msevent.dto.response.SessionResponse;
import com.example.msevent.dto.response.SpeakerResponse;
import com.example.msevent.model.Event;
import com.example.msevent.model.Session;
import com.example.msevent.model.Speaker;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEntity(EventRequest request);

    EventResponse toDto(Event entity);

    void updateFromDto(EventRequest request, @MappingTarget Event entity);

    Session toEntity(SessionRequest request);

    SessionResponse toDto(Session entity);

    SpeakerResponse toDto(Speaker entity);

    List<SessionResponse> toSessionResponseList(List<Session> sessions);
}
