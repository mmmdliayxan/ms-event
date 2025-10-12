package com.example.msevent.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class SessionResponse {

    private Long id;
    private String title;
    private String room;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private List<SpeakerResponse> speakers;

}
