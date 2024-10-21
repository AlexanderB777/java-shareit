package ru.practicum.request.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.util.config.FeignClientConfig;

import java.util.List;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@FeignClient(url = "${shareit-server.url}",
        path = "/requests",
        name = "itemRequestClient",
        configuration = FeignClientConfig.class)
public interface ItemRequestClient {

    @PostMapping
    ItemRequestDtoResponse create(@RequestBody ItemRequestDto dto,
                                  @RequestHeader(USER_ID_HEADER) Long userId);

    @GetMapping
    List<ItemRequestDtoResponse> getPersonalRequests(@RequestHeader(USER_ID_HEADER) Long userId);

    @GetMapping("/all")
    List<ItemRequestDtoResponse> getAllRequests();

    @GetMapping("/{requestId}")
    ItemRequestDtoResponse getRequestById(@PathVariable Long requestId);
}