package ru.practicum.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.item.service.ItemService;
import ru.practicum.util.exception.ItemNotAvailableException;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.OutOfPermissionException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceImplTest {
    @Autowired
    private ItemService itemService;

    @SpyBean
    private ItemRepository itemRepository;

    @Test
    @Transactional
    @DisplayName("Создание новой вещи")
    @Sql(statements = "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');")
    void testCreate_CreateNewItem() {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        // when
        ItemDtoResponse persistedItem = itemService.create(itemDto, 1L);

        // then
        assertNotNull(persistedItem.getId());
        assertEquals(itemDto.getName(), persistedItem.getName());
        assertEquals(itemDto.getDescription(), persistedItem.getDescription());
        assertEquals(itemDto.getAvailable(), persistedItem.getAvailable());
    }

    @Test
    @Transactional // проверить
    @DisplayName("Создание новой вещи для несуществующего пользователя")
    void testCreate_CreateNewItemForNotExistingUser() {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        // when & then
        var exception = assertThrows(NotFoundException.class, () -> itemService.create(itemDto, 1L));
        assertEquals(NotFoundException.class, exception.getClass());
        assertEquals("User not found", exception.getMessage());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @Transactional
    @DisplayName("Обновление существующей вещи")
    @Sql(statements = {
            "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) "})
    void testUpdate_UpdateExistingItem() {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        // when
        ItemDtoResponse persistedItem = itemService.update(itemDto, 1L, 1L);

        // then
        assertNotNull(persistedItem.getId());
        assertEquals(itemDto.getName(), persistedItem.getName());
        assertEquals(itemDto.getDescription(), persistedItem.getDescription());
        assertEquals(itemDto.getAvailable(), persistedItem.getAvailable());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    @Transactional
    @DisplayName("Обновление вещи чужим пользователем")
    @Sql(statements = {
            "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) "})
    void testUpdate_UpdateByNotOwner() {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        // when & then
        var exception = assertThrows(OutOfPermissionException.class,
                () -> itemService.update(itemDto, 2L, 1L));
        assertEquals("User does not have permission to this item", exception.getMessage());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @Transactional
    @DisplayName("Обновление несуществующей вещи")
    @Sql(statements = {
            "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) "})
    void testUpdate_NotExistingItem() {
        // given
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setDescription("description");
        itemDto.setAvailable(false);

        // when & then
        var exception = assertThrows(NotFoundException.class,
                () -> itemService.update(itemDto, 1L, 2L));
        assertEquals("Item not found", exception.getMessage());
        verify(itemRepository, times(1)).findById(2L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @Transactional
    @DisplayName("Получение существующей вещи по ID")
    @Sql(statements = {
            "INSERT INTO users VALUES (1, 'Alexander', 'alex@gmail.com');",
            "INSERT INTO users VALUES (2, 'Alexey', 'alexey@gmail.com')",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) ",
            "INSERT INTO comments VALUES (1, 'cool', 1, 1)",
            "INSERT INTO comments VALUES (2, 'not cool', 1, 2)"})
    void testGetById_ExistingItem() {
        // when
        ItemDtoResponse persistedItem = itemService.getById(1L);

        // then
        assertEquals(1L, persistedItem.getId());
        assertEquals("Thing", persistedItem.getName());
        assertEquals("thing description", persistedItem.getDescription());
        assertEquals(true, persistedItem.getAvailable());
        Set<String> comments = Set.of("not cool", "cool");
        assertEquals(comments, Set.copyOf(persistedItem.getComments()));
    }

    @Test
    @DisplayName("Получение несуществующей вещи по ID")
    void testGetById_NotExistingItem() {
        // when & then
        var exception = assertThrows(NotFoundException.class, () -> itemService.getById(1L));
        assertEquals("Item not found", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Получение списка вещей пользователя")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) ",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (2, 'Another thing', 'another thing description', FALSE, 1) "
    })
    void testGetAllFromUser() {
        // when
        List<ItemDtoResponse> persistedItems = itemService.getAllFromUser(1L);

        // then
        assertEquals(2, persistedItems.size());
        assertEquals(1L, persistedItems.get(0).getId());
        assertEquals("Thing", persistedItems.get(0).getName());
        assertEquals("thing description", persistedItems.get(0).getDescription());
        assertEquals(true, persistedItems.get(0).getAvailable());
        assertEquals(2L, persistedItems.get(1).getId());
        assertEquals("Another thing", persistedItems.get(1).getName());
        assertEquals("another thing description", persistedItems.get(1).getDescription());
        assertEquals(false, persistedItems.get(1).getAvailable());
    }

    @Test
    @DisplayName("Получение списка вещей несуществующего пользователя")
    void testGetAllFromUser_NotExistingUser() {
        var exception = assertThrows(NotFoundException.class, () -> itemService.getAllFromUser(1L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Поиск существующей вещи")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'ALEX', 'alex@gmail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) "
    })
    void testSearch_ExistingItem() {
        // when
        List<ItemDtoResponse> persistedItems = itemService.search("ing");

        // then
        assertEquals(1, persistedItems.size());
        assertEquals(1L, persistedItems.getFirst().getId());
    }

    @Test
    @DisplayName("Поиск несуществующей вещи")
    void testSearch_NotExistingItem() {
        // when
        List<ItemDtoResponse> persistedItems = itemService.search("ttt");

        // then
        assertEquals(0, persistedItems.size());
    }

    @Test
    @Transactional
    @DisplayName("Поиск нескольких вещей по описанию")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (1, 'Thing', 'thing description', TRUE, 1) ",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES (2, 'Another thing', 'another thing description', TRUE, 1) "
    })
    void testSearch_TwoItems() {
        // when
        List<ItemDtoResponse> persistedItems = itemService.search("description");
        // then
        assertEquals(2, persistedItems.size());
        assertEquals(1L, persistedItems.get(0).getId());
        assertEquals(2L, persistedItems.get(1).getId());
    }

    @Test
    @Transactional
    @DisplayName("Создание комментария")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'user', 'user@mail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES ( 1, 'thing', 'thing description', TRUE, 1 )",
            "INSERT INTO bookings (id, start_date, end_date, item_id, booker_id, status) " +
                    "VALUES (" +
                    "1, " +
                    "CURRENT_TIMESTAMP - INTERVAL '1' MONTH, " +
                    "CURRENT_TIMESTAMP - INTERVAL '1' DAY, " +
                    "1, " +
                    "1, " +
                    "'APPROVED')"
    })
    void testCreateComment() {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setText("cool");

        // when
        CommentDto createdComment = itemService.createComment(1L, commentDto, 1L);

        // then
        assertEquals(1L, createdComment.getId());
        assertEquals("user", createdComment.getAuthorName());
        assertEquals("cool", createdComment.getText());
        assertEquals(1, itemService.getById(1L).getComments().size());
    }

    @Test
    @Transactional
    @DisplayName("Создание комментария для незавершенного бронирования")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'user', 'user@mail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES ( 1, 'thing', 'thing description', TRUE, 1 )",
            "INSERT INTO bookings (id, start_date, end_date, item_id, booker_id, status) " +
                    "VALUES (" +
                    "1, " +
                    "CURRENT_TIMESTAMP - INTERVAL '1' MONTH, " +
                    "CURRENT_TIMESTAMP + INTERVAL '1' DAY, " +
                    "1, " +
                    "1, " +
                    "'APPROVED');\n"
    })
    void testCreateComment_NotFinishedBooking() {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setText("cool");

        // when & then
        var exception = assertThrows(ItemNotAvailableException.class,
                () -> itemService.createComment(1L, commentDto, 1L));
        assertEquals("Not allowed for current booking", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Создание комментария для несуществующего бронирования")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'user', 'user@mail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES ( 1, 'thing', 'thing description', TRUE, 1 )"
    })
    void testCreateBooking_NotExistingBooking() {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setText("cool");

        // when & then
        var exception = assertThrows(NotFoundException.class,
                () -> itemService.createComment(1L, commentDto, 1L));
        assertEquals("Booking with this item not exists", exception.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Создание комментария чужим пользователем")
    @Sql(statements = {
            "INSERT INTO users VALUES ( 1, 'user', 'user@mail.com' )",
            "INSERT INTO items (id, name, description, available, owner_id) " +
                    "VALUES ( 1, 'thing', 'thing description', TRUE, 1 )",
            "INSERT INTO bookings (id, start_date, end_date, item_id, booker_id, status) " +
                    "VALUES (" +
                    "1, " +
                    "CURRENT_TIMESTAMP - INTERVAL '1' MONTH, " +
                    "CURRENT_TIMESTAMP + INTERVAL '1' DAY, " +
                    "1, " +
                    "1, " +
                    "'APPROVED');\n"
    })
    void testCreateComment_anotherUser() {
        // given
        CommentDto commentDto = new CommentDto();
        commentDto.setText("cool");

        // when & then
        var exception = assertThrows(ItemNotAvailableException.class,
                () -> itemService.createComment(1L, commentDto, 11L));
        assertEquals("Not allowed for current booking", exception.getMessage());
    }
}