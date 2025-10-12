package com.example.msevent.dto.response;

import lombok.Data;

@Data
public class SpeakerResponse {

    private Long id;
    private String fullName;
    private String bio;
    private String company;
    private String position;

}
