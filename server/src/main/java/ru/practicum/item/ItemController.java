package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.item.service.ItemService;

import java.util.List;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDtoResponse create(@RequestHeader(USER_ID_HEADER) Long userId,
                                  @RequestBody ItemDto itemDto) {
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse update(@RequestHeader(USER_ID_HEADER) Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Long itemId) {
        return service.update(itemDto, userId, itemId);
    }

    @GetMapping("/{id}")
    public ItemDtoResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ItemDtoResponse> getAllFromUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return service.getAllFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam String text) {
        return service.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.createComment(itemId, commentDto, userId);
    }
}