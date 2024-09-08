package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    Optional<Item> findById(long id);

    List<Item> findAllByUserId(long userId);

    List<Item> search(String query);
}
