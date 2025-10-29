package com.example.msevent.client;

import com.example.msevent.config.FeignAuthInterceptor;
import com.example.msevent.dto.request.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authClient", url = "http://ms-auth:8081/api/users", configuration = FeignAuthInterceptor.class)
public interface UserClient {
    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id);
}
