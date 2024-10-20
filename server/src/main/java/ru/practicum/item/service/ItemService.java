package ru.practicum.item.service;

import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;

import java.util.List;

public interface ItemService {
    ItemDtoResponse create(ItemDto itemDto, long userId);

    ItemDtoResponse update(ItemDto itemDto, long userId, long itemId);

    ItemDtoResponse getById(long itemId);

    List<ItemDtoResponse> getAllFromUser(long userId);

    List<ItemDtoResponse> search(String text);

    CommentDto createComment(Long itemId, CommentDto commentDto, Long userId);
}