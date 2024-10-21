package ru.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.api.ItemClient;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.util.Marker;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @RequestBody @Validated({Marker.OnCreate.class}) ItemDto itemDto) {
        return client.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<?> update(@RequestHeader(USER_ID_HEADER) Long userId,
                                    @RequestBody @Validated({Marker.OnUpdate.class}) ItemDto itemDto,
                                    @PathVariable Long itemId) {
        return client.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        return client.getItem(id);
    }

    @GetMapping
    public ResponseEntity<?> getAllFromUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return client.getAllFromUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String text) {
        return client.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long itemId,
                                           @RequestBody @Valid CommentDto commentDto,
                                           @RequestHeader(USER_ID_HEADER) Long userId) {
        return client.createComment(itemId, commentDto, userId);
    }
}