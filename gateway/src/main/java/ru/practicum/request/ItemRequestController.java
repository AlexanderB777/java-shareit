package ru.practicum.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.api.ItemRequestClient;
import ru.practicum.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient client;

    @PostMapping
    public ItemRequestDto create(@RequestBody @Valid ItemRequestDto dto,
                                 @RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        return client.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getPersonalRequests(@RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        return client.getPersonalRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return client.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId) {
        return client.getRequestById(requestId);
    }
}