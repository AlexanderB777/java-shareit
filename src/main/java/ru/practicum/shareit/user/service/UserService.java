package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto, long userId);

    UserDto getById(long id);

    void delete(long id);
}