package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.util.NotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto save(UserDto userDto) {
        return mapper.toDto(repository.save(mapper.toEntity(userDto)));
    }

    @Override
    public UserDto update(UserDto userDto, long userId) {
        User user = repository.get(userId).orElseThrow(() -> new NotFoundException("Пользователя не существует"));
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return mapper.toDto(user);
    }

    @Override
    public UserDto getById(long id) {
        return mapper.toDto(repository
                .get(id).orElseThrow(() -> new NotFoundException("Пользователя не существует")));
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public boolean existEmail(String email) {
        return repository.existsEmail(email);
    }
}
