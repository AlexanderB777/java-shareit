package ru.practicum.request.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.util.config.FeignClientConfig;

import java.util.List;

@FeignClient(url = "${shareit-server.url}",
        path = "/requests",
        name = "itemRequestClient",
        configuration = FeignClientConfig.class)
public interface ItemRequestClient {
    String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    ItemRequestDto create(@RequestBody ItemRequestDto dto,
                          @RequestHeader(USER_ID_HEADER) Long userId);

    @GetMapping
    List<ItemRequestDto> getPersonalRequests(@RequestHeader(USER_ID_HEADER) Long userId);

    @GetMapping("/all")
    List<ItemRequestDto> getAllRequests();

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@PathVariable Long requestId);
}