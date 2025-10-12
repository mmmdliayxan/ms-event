package com.example.msevent.client;

import com.example.msevent.dto.request.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authClient", url = "http://localhost:8080/api/users")
public interface UserClient {
    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}
