package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

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
}