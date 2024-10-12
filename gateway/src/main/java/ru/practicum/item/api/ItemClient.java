package ru.practicum.item.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.config.FeignClientConfig;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

@FeignClient(url = "${shareit-server.url}",
        path = "/items",
        name = "itemClient",
        configuration = FeignClientConfig.class)
public interface ItemClient {
    String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    ResponseEntity<?> createItem(@RequestHeader(USER_ID_HEADER) long ownerId,
                                 @RequestBody ItemDto itemDto);

    @PatchMapping("/{itemId}")
    ResponseEntity<?> updateItem(@RequestHeader(USER_ID_HEADER) Long userId,
                                 @RequestBody ItemDto itemDto,
                                 @PathVariable long itemId);

    @GetMapping("/{itemId}")
    ResponseEntity<?> getItem(@PathVariable Long itemId);

    @GetMapping
    ResponseEntity<?> getAllFromUser(@RequestHeader(USER_ID_HEADER) Long userId);

    @GetMapping("/search")
    ResponseEntity<?> search(@RequestParam String text);

    @PostMapping("/{itemId}/comment")
    ResponseEntity<?> createComment(@PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId);
}