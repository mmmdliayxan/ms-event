package com.example.msevent.dto.request;


import com.example.msevent.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class EventRequest {

    @NotBlank
    private String title;

    private String description;

    private String location;

    @NotNull
    private OffsetDateTime startAt;

    @NotNull
    private OffsetDateTime endAt;

    @NotNull
    private Category category;

    private List<SessionRequest> sessions;

}
