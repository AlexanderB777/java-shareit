package ru.practicum.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.api.ItemRequestClient;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;

import java.util.List;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ItemRequestDtoResponse create(@RequestBody @Valid ItemRequestDto dto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        return client.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoResponse> getPersonalRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return client.getPersonalRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> getAllRequests() {
        return client.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse getRequestById(@PathVariable Long requestId) {
        return client.getRequestById(requestId);
    }
}