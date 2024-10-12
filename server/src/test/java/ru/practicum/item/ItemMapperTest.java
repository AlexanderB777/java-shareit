package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        itemMapper = Mappers.getMapper(ItemMapper.class);
    }

    @Test
    public void toEntity_shouldMapDtoToEntity() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");

        Item item = itemMapper.toEntity(itemDto);

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
    }

    @Test
    public void toDto_shouldMapEntityToDto() {
        ItemRequest request = new ItemRequest();
        request.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setItemRequest(request);

        ItemDto itemDto = itemMapper.toDto(item);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("Test Item", itemDto.getName());
        assertEquals("Test Description", itemDto.getDescription());
        assertEquals(2L, itemDto.getRequestId()); // Проверка маппинга requestId
    }

    @Test
    public void toDtoList_shouldMapListOfEntitiesToDtoList() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        List<Item> items = List.of(item1, item2);

        List<ItemDto> itemDtos = itemMapper.toDtoList(items);

        assertNotNull(itemDtos);
        assertEquals(2, itemDtos.size());
        assertEquals(1L, itemDtos.get(0).getId());
        assertEquals("Item 1", itemDtos.get(0).getName());
        assertEquals(2L, itemDtos.get(1).getId());
        assertEquals("Item 2", itemDtos.get(1).getName());
    }
}