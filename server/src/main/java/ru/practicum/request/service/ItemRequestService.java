package ru.practicum.request.service;

import ru.practicum.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId);

    List<ItemRequestDto> getPersonalRequests(Long userId);

    List<ItemRequestDto> getAllRequests();

    ItemRequestDto getById(Long id);
}