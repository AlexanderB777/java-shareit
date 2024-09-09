package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> get(long id);

    void delete(long id);

    boolean existsEmail(String email);
}