package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.ValidationException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto save(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException("Conflict email");
        }
        return mapper.toDto(repository.save(mapper.toEntity(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, long userId) {
        User user = repository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException("Conflict email");
        }
        return mapper.toDto(user);
    }

    @Override
    public UserDto getById(long id) {
        return mapper.toDto(repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}