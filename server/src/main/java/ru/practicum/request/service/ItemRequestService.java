package ru.practicum.request.service;

import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoResponse create(ItemRequestDto itemRequestDto, Long requesterId);

    List<ItemRequestDtoResponse> getPersonalRequests(Long userId);

    List<ItemRequestDtoResponse> getAllRequests();

    ItemRequestDtoResponse getById(Long id);
}