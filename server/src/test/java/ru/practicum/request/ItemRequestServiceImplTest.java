package ru.practicum.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.request.service.ItemRequestServiceImpl;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setCreated(LocalDate.now());
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setCreated(LocalDate.now());
    }

    @Test
    void create_shouldCreateItemRequest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRequestMapper.toEntity(itemRequestDto)).thenReturn(itemRequest);
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestDto);

        ItemRequestDto createdRequest = itemRequestService.create(itemRequestDto, userId);

        assertEquals(itemRequestDto.getId(), createdRequest.getId());
        assertEquals(itemRequestDto.getCreated(), createdRequest.getCreated());
        verify(userRepository).findById(userId);
        verify(itemRequestMapper).toEntity(itemRequestDto);
        verify(itemRequestRepository).save(itemRequest);
        verify(itemRequestMapper).toDto(itemRequest);
    }

    @Test
    void create_shouldThrowNotFoundException_whenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemRequestService.create(itemRequestDto, userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getById_shouldReturnItemRequestDto() {
        Long requestId = 1L;
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toDto(itemRequest)).thenReturn(itemRequestDto);

        ItemRequestDto foundRequest = itemRequestService.getById(requestId);

        assertEquals(itemRequestDto.getId(), foundRequest.getId());
        verify(itemRequestRepository).findById(requestId);
        verify(itemRequestMapper).toDto(itemRequest);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenRequestNotFound() {
        Long requestId = 1L;
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getById(requestId);
        });

        assertEquals("Request not found", exception.getMessage());
    }
}