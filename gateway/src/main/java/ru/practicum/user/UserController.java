package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.api.UserClient;
import ru.practicum.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto) {
        return client.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        return client.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable long userId) {
        return client.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        return client.deleteUser(userId);
    }
}