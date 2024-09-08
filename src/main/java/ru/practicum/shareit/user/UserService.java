package ru.practicum.shareit.user;

public interface UserService {

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto, long userId);

    UserDto getById(long id);

    void delete(long id);

    boolean existEmail(String email);
}
