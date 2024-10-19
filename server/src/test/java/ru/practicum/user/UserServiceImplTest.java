package ru.practicum.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @SpyBean
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("Создание нового пользователя")
    void testSave_createNewUser() {
        // given
        UserDto userDto = new UserDto();
        userDto.setName("Alex");
        userDto.setEmail("alex@gmail.com");

        // when
        UserDto persistedUser = userService.save(userDto);

        // then
        assertNotNull(persistedUser.getId());
        assertEquals("Alex", persistedUser.getName());
        assertEquals("alex@gmail.com", persistedUser.getEmail());
    }

    @Test
    @Transactional
    @DisplayName("Создание пользователя с конфликтом Email адреса")
    @Sql(statements = "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');")
    void testSave_createNewUserConflictEmail() {
        // given
        UserDto userDto = new UserDto();
        userDto.setName("Alex");
        userDto.setEmail("alex@gmail.com");

        // when & then
        var exception = assertThrows(ValidationException.class, () -> userService.save(userDto));
        assertEquals("Conflict email", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @Transactional
    @DisplayName("Обновление пользователя")
    @Sql(statements = "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )")
    void testUpdate_ExistingUser() {
        // given
        UserDto userDto = new UserDto();
        userDto.setName("AlexUpdated");

        // when
        UserDto updatedUser = userService.update(userDto, 1L);

        // then
        assertEquals("AlexUpdated", updatedUser.getName());
    }

    @Test
    @Transactional
    @DisplayName("Обновление несуществующего пользователя")
    void testUpdate_NotExistingUser() {
        // given
        UserDto userDto = new UserDto();
        userDto.setName("AlexUpdated");

        // when & then
        var exception = assertThrows(NotFoundException.class, () -> userService.update(userDto, 1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Получение существующего пользователя по id")
    @Sql(statements = "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )")
    void testGetById_ExistingUser() {
        // when
        UserDto userDto = userService.getById(1L);

        // then
        assertEquals("Alex", userDto.getName());
        assertEquals("alex@gmail.com", userDto.getEmail());
    }

    @Test
    @DisplayName("Получение несуществующего пользователя по id")
    void testGetById_NotExistingUser() {
        // when & then
        var exception = assertThrows(NotFoundException.class, () -> userService.getById(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Удаление пользователя")
    @Sql(statements = "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )")
    void testDelete() {
        // when
        userService.delete(1L);

        // then
        verify(userRepository, times(1)).deleteById(anyLong());
        var exception = assertThrows(NotFoundException.class, () -> userService.getById(1L));
        assertEquals("User not found", exception.getMessage());
    }
}