package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.ItemNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.OutOfPermissionException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        Item newItem = mapper.toEntity(itemDto);
        newItem.setOwner(userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found")));
        Item persisted = repository.save(newItem);
        return mapper.toDto(persisted);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId, long itemId) {
        Item foundItem = repository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (foundItem.getOwner().getId() != userId) {
            throw new OutOfPermissionException("User does not have permission to this item");
        }
        if (itemDto.getName() != null) {
            foundItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            foundItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            foundItem.setAvailable(itemDto.getAvailable());
        }
        return mapper.toDto(repository.save(foundItem));
    }

    @Override
    public ItemDto getById(long itemId) {
        Item item = repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
        ItemDto itemDto = mapper.toDto(item);
        itemDto.setComments(commentRepository.getCommentsTextByItemId(itemId));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllFromUser(long userId) {
        return mapper.toDtoList(repository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return mapper.toDtoList(repository.searchByQuery(text));
    }

    @Override
    public CommentDto createComment(Long itemId, CommentDto commentDto, Long userId) {
        if (!bookingRepository.existsByBookerIdAndItemIdPast(userId, itemId)) {
            throw new ItemNotAvailableException("Not allowed for current booking");
        }
        Comment comment = new Comment();
        comment.setItem(repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
        comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found")));
        comment.setText(commentDto.getText());
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }
}