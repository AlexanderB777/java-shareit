package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.NotFoundException;
import ru.practicum.shareit.util.ValidationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto save(UserDto userDto) {
        checkDuplicateEmail(userDto.getEmail());
        return mapper.toDto(repository.save(mapper.toEntity(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, long userId) {
        checkDuplicateEmail(userDto.getEmail());
        User user = repository
                .findById(userId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return mapper.toDto(user);
    }

    @Override
    public UserDto getById(long id) {
        return mapper.toDto(repository
                .findById(id).orElseThrow(() -> new NotFoundException("Пользователя не существует")));
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    private void checkDuplicateEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new ValidationException("User with email already exists");
        }
    }
}
