package ru.practicum.shareit.item;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemDto itemDto);
    ItemDto toDto(Item item);
    List<ItemDto> toDtoList(List<Item> items);
}
