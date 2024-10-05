package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) @NotNull Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        return service.update(itemDto, userId, itemId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ItemDto> getAllFromUser(@RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        return service.getAllFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return service.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                    @RequestBody @Valid CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        return service.createComment(itemId, commentDto, userId);
    }
}