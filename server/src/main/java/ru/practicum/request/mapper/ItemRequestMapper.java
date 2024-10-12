package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ItemRequestMapper {
//    @Mapping(target = "items", ignore = true)
    ItemRequestDto toDto(ItemRequest itemRequest);

//    @Mapping(target = "items", ignore = true)
    List<ItemRequestDto> toDto(List<ItemRequest> itemRequests);

    ItemRequest toEntity(ItemRequestDto itemRequestDto);

    List<ItemRequest> toEntity(List<ItemRequestDto> itemRequestDto);
}