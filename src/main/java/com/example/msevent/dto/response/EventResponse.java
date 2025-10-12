package com.example.msevent.dto.response;

import com.example.msevent.dto.request.UserDto;
import com.example.msevent.model.Category;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;


@Data
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private Category category;

    private List<SessionResponse> sessions;
    private Long totalParticipants;
    private UserDto createdBy;
}
