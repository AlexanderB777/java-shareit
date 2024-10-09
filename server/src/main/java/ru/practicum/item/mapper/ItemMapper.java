package ru.practicum.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item toEntity(ItemDto itemDto);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemDto toDto(Item item);

    List<ItemDto> toDtoList(List<Item> items);
}