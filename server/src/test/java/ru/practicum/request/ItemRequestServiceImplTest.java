package ru.practicum.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Sql(statements = "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = "DELETE FROM users",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ItemRequestServiceImplTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @SpyBean
    private ItemRequestRepository itemRequestRepository;

    @Test
    @Transactional
    @DisplayName("Создание нового запроса")
    void testCreate() {
        // given
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");

        // when
        ItemRequestDtoResponse request = itemRequestService.create(itemRequestDto, 1L);

        // then
        assertEquals(request.getDescription(), "description");
        assertEquals(LocalDate.now(), request.getCreated());
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    @DisplayName("Создание нового запроса несуществующим пользователем")
    void testCreate_NotExistingUser() {
        // given
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");

        // when & then
        Exception exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.create(itemRequestDto, 500L));
        assertEquals("User not found", exception.getMessage());
        verify(itemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    @Transactional
    @DisplayName("Получение списка запросов существующего пользователя")
    @Sql(statements = {
            "INSERT INTO requests VALUES ( 1, 'description1', 1, CURRENT_TIMESTAMP() )",
            "INSERT INTO requests VALUES ( 2, 'description2', 1, CURRENT_TIMESTAMP() )"
    })
    void testGetPersonalRequests() {
        // when
        List<ItemRequestDtoResponse> requests = itemRequestService.getPersonalRequests(1L);

        // then
        assertEquals(2, requests.size());
        verify(itemRequestRepository, times(1)).findByRequestor_Id(anyLong());
    }

    @Test
    @DisplayName("Получение списка запросов несуществующего пользователя")
    void testGetPersonalRequests_NotExistingUser() {
        // when & then
        Exception exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.getPersonalRequests(500L));
        assertEquals("User not found", exception.getMessage());
        verify(itemRequestRepository, never()).findByRequestor_Id(anyLong());
    }

    @Test
    @DisplayName("Получение пустого списка запросов")
    void testGetPersonalRequests_NoRequests() {
        // when
        List<ItemRequestDtoResponse> requests = itemRequestService.getPersonalRequests(1L);

        // then
        assertEquals(0, requests.size());
        verify(itemRequestRepository, times(1)).findByRequestor_Id(anyLong());
    }

    @Test
    @Transactional
    @DisplayName("Получение списка всех запросов")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 2, 'Andrew', 'andrew@gmail.com' )",
            "INSERT INTO requests VALUES ( 1, 'description1', 1, CURRENT_TIMESTAMP() )",
            "INSERT INTO requests VALUES ( 2, 'description2', 2, CURRENT_TIMESTAMP() )"
    })
    void testGetAllRequests() {
        // when
        List<ItemRequestDtoResponse> requests = itemRequestService.getAllRequests();

        // then
        assertEquals(2, requests.size());
        verify(itemRequestRepository, times(1)).findAllByOrderByCreatedDesc();
    }

    @Test
    @DisplayName("Получение пустого списка всех запросов")
    void testGetAllRequests_NoRequests() {
        // when
        List<ItemRequestDtoResponse> requests = itemRequestService.getAllRequests();

        // then
        assertEquals(0, requests.size());
        verify(itemRequestRepository, times(1)).findAllByOrderByCreatedDesc();
    }

    @Test
    @Transactional
    @DisplayName("Получение запроса по id")
    @Sql(statements = "INSERT INTO requests VALUES ( 1, 'description1', 1, CURRENT_TIMESTAMP() )")
    void testGetById() {
        // when
        ItemRequestDtoResponse itemRequestDto = itemRequestService.getById(1L);

        // then
        assertEquals(1, itemRequestDto.getId());
        assertEquals("description1", itemRequestDto.getDescription());
    }
}