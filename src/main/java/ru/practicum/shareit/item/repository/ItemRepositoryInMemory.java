package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryInMemory {
    Item save(Item item);

    Optional<Item> findById(long id);

    List<Item> findAllByUserId(long userId);

    List<Item> search(String query);
}
