package ru.practicum.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserServiceImpl;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    @Test
    public void save_shouldReturnSavedUser_whenEmailDoesNotExist() {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto savedUser = userService.save(userDto);

        assertEquals(savedUser.getId(), userDto.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void save_shouldThrowValidationException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        ValidationException exception =
                org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class, () -> {
                    userService.save(userDto);
                });

        assertEquals("Conflict email", exception.getMessage());
    }

    @Test
    public void update_shouldReturnUpdatedUser() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto updatedUser = userService.update(userDto, 1L);

        assertEquals(updatedUser.getId(), userDto.getId());
        verify(userRepository).findById(anyLong());
    }

    @Test
    public void update_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        NotFoundException exception =
                org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
                    userService.update(userDto, 999L);
                });

        assertEquals("Пользователя не существует", exception.getMessage());
    }

    @Test
    public void getById_shouldReturnUser() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        UserDto foundUser = userService.getById(1L);

        assertEquals(foundUser.getId(), userDto.getId());
    }

    @Test
    public void getById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        NotFoundException exception =
                org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> {
                    userService.getById(999L);
                });

        assertEquals("Пользователя не существует", exception.getMessage());
    }

    @Test
    public void delete_shouldCallDeleteMethod() {
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }
}