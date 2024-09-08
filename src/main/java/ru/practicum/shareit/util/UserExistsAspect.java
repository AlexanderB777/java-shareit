package ru.practicum.shareit.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserRepository;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserExistsAspect {
    private final UserRepository repository;

    @Before("execution(* ru.practicum.shareit.item.ItemController.create(..)) && args(userId, ..) " +
            "|| execution(* ru.practicum.shareit.item.ItemController.update(..)) && args(userId, ..)")
    public void checkUserExists(long userId) {
        log.info("Checking if user {} exists", userId);
        if(repository.get(userId).isEmpty()) {
            log.info("User {} does not exist", userId);
            throw new NotFoundException("User " + userId + " does not exist");
        }
    }
}
