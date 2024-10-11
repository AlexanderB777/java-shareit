package ru.practicum.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long userId);

    @Query(value = """
            SELECT i FROM Item i
            WHERE (LOWER(i.name) LIKE CONCAT('%', LOWER(:text), '%')
               OR LOWER(i.description) LIKE CONCAT('%', LOWER(:text), '%'))
               AND i.available = TRUE
            """)
    List<Item> searchByQuery(String text);

    List<Item> findItemsByItemRequest(ItemRequest itemRequest);
}