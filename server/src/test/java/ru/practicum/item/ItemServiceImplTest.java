package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.mapper.CommentMapper;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.CommentRepository;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.item.service.ItemServiceImpl;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.ItemNotAvailableException;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.OutOfPermissionException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ItemServiceImplTest {

    private ItemServiceImpl itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;
    private ItemRequestRepository itemRequestRepository;
    private ItemMapper itemMapper;
    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        commentRepository = mock(CommentRepository.class);
        bookingRepository = mock(BookingRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemMapper = mock(ItemMapper.class);
        commentMapper = mock(CommentMapper.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, itemRequestRepository, itemMapper, commentMapper);
    }

    @Test
    public void create_shouldCreateNewItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setRequestId(1L);

        User user = new User();
        user.setId(1L);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemMapper.toEntity(any(ItemDto.class))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        ItemDto result = itemService.create(itemDto, 1L);

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void update_shouldUpdateItem() {
        long userId = 1L;
        long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(any(Item.class))).thenReturn(itemDto);

        ItemDto result = itemService.update(itemDto, userId, itemId);

        assertNotNull(result);
        assertEquals("Updated Item", result.getName());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    public void update_shouldThrowOutOfPermissionException_whenUserIsNotOwner() {
        long userId = 2L;
        long itemId = 1L;
        ItemDto itemDto = new ItemDto();

        User user = new User();
        user.setId(1L); // Владелец с id 1L

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(OutOfPermissionException.class, () -> {
            itemService.update(itemDto, userId, itemId);
        });

        verify(itemRepository, times(0)).save(any(Item.class));
    }

    @Test
    public void getById_shouldReturnItemDto() {
        long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(itemDto);
        when(commentRepository.getCommentsTextByItemId(itemId)).thenReturn(Collections.emptyList());

        ItemDto result = itemService.getById(itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        verify(commentRepository, times(1)).getCommentsTextByItemId(itemId);
    }

    @Test
    public void getById_shouldThrowNotFoundException_whenItemDoesNotExist() {
        long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.getById(itemId);
        });

        verify(commentRepository, times(0)).getCommentsTextByItemId(itemId);
    }

    @Test
    public void createComment_shouldCreateComment_whenBookingExists() {
        long itemId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Nice item");

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);

        Comment comment = new Comment();
        comment.setText("Nice item");

        when(bookingRepository.existsByBookerIdAndItemIdPast(userId, itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDto(any(Comment.class))).thenReturn(commentDto);

        CommentDto result = itemService.createComment(itemId, commentDto, userId);

        assertNotNull(result);
        assertEquals("Nice item", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void createComment_shouldThrowItemNotAvailableException_whenBookingDoesNotExist() {
        long itemId = 1L;
        long userId = 1L;
        CommentDto commentDto = new CommentDto();

        when(bookingRepository.existsByBookerIdAndItemIdPast(userId, itemId)).thenReturn(false);

        assertThrows(ItemNotAvailableException.class, () -> {
            itemService.createComment(itemId, commentDto, userId);
        });

        verify(commentRepository, times(0)).save(any(Comment.class));
    }
}