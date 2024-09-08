package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final List<Item> items = new ArrayList<>();


    public Item save(Item item) {
        item.setId(getId());
        items.add(item);
        return item;
    }

    public Optional<Item> findById(long id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst();
    }

    @Override
    public List<Item> findAllByUserId(long userId) {
        return items.stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public List<Item> search(String query) {
        return items.stream()
                .filter(item -> (item.getName().toLowerCase().contains(query.toLowerCase())
                        || item.getDescription().toLowerCase().contains(query.toLowerCase()))
                        && item.getAvailable())
                .toList();
    }

    private long getId() {
        long maxId = items.stream().mapToLong(Item::getId).max().orElse(0L);
        return maxId + 1;
    }
}
