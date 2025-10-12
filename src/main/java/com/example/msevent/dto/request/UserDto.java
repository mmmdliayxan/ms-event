package com.example.msevent.dto.request;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private String role;
    private String status;
}
