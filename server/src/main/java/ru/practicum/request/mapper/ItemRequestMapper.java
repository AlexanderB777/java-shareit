package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemRequestMapper {
    ItemRequestDtoResponse toDto(ItemRequest itemRequest);

    List<ItemRequestDtoResponse> toDto(List<ItemRequest> itemRequests);

    ItemRequest toEntity(ItemRequestDto itemRequestDto);
}