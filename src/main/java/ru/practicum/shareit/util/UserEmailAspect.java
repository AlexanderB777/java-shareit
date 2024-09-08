package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserEmailAspect {
    private final UserService service;

    @Before("execution(* ru.practicum.shareit.user.UserController.create(..)) && args(userDto) " +
            "|| execution(* ru.practicum.shareit.user.UserController.update(..)) && args(userDto, ..)")
    public void checkDuplicateEmail(UserDto userDto) {
        log.info("Checking if user with email already exists: " + userDto.getEmail());

        if (service.existEmail(userDto.getEmail())) {
            throw new ValidationException("User with email already exists");
        }
    }
}