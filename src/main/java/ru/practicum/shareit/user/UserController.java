package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Create user: {}", userDto);
        return service.save(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Update user: {}", userDto);
        return service.update(userDto, userId);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        log.info("Get user: {}", id);
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("Delete user: {}", id);
        service.delete(id);
    }
}