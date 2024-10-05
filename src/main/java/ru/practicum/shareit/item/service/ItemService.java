package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId, long itemId);

    ItemDto getById(long itemId);

    List<ItemDto> getAllFromUser(long userId);

    List<ItemDto> search(String text);

    CommentDto createComment(Long itemId, CommentDto commentDto, Long userId);
}