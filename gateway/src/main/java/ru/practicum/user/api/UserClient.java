package ru.practicum.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.config.FeignClientConfig;
import ru.practicum.user.dto.UserDto;

@FeignClient(url = "${shareit-server.url}",
        path = "/users",
        name = "userClient",
        configuration = FeignClientConfig.class)
public interface UserClient {

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserDto userDto);

    @PatchMapping("/{userId}")
    ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId);

    @GetMapping("/{userId}")
    ResponseEntity<?> getUser(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable Long userId);
}