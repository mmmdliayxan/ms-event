package com.example.msevent.dto.request;

import com.example.msevent.model.Role;
import com.example.msevent.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String password;
    private String email;
    private Role role;
    private UserStatus status;
}
