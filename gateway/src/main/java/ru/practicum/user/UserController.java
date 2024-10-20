package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.api.UserClient;
import ru.practicum.user.dto.UserDto;
import ru.practicum.util.Marker;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Validated(Marker.OnCreate.class) UserDto userDto) {
        return client.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<?> updateUser(@RequestBody @Validated(Marker.OnUpdate.class) UserDto userDto,
                                        @PathVariable long userId) {
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