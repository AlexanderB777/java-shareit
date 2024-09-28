package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.util.NotFoundException;
import ru.practicum.shareit.util.OutOfPermissionException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        Item newItem = mapper.toEntity(itemDto);
        newItem.setOwnerId(userId);
        Item persisted = repository.save(newItem);
        return mapper.toDto(persisted);
    }

    @Override
    public ItemDto update(ItemDto itemDto, long userId, long itemId) {
        Item foundItem = repository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (foundItem.getOwnerId() != userId) {
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
        return mapper.toDto(repository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
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
}