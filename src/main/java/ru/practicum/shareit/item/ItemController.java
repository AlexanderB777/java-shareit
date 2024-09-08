package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                              @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating new item: {}", itemDto);
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long id) {
        log.info("Updating existing item: {}, with id = {}", itemDto, id);
        return service.update(itemDto, userId, id);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        log.info("Retrieving existing item: {}", id);
        return service.getById(id);
    }

    @GetMapping
    public List<ItemDto> getAllFromUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Retrieving all items from user: {}", userId);
        return service.getAllFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Searching for items with text {}", text);
        return service.search(text);
    }
}
