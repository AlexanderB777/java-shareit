package ru.practicum.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.api.ItemRequestClient;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.util.Constants;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ItemRequestDtoResponse create(@RequestBody @Valid ItemRequestDto dto,
                                         @RequestHeader(Constants.USER_ID_HEADER) Long userId) {
        return client.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoResponse> getPersonalRequests(@RequestHeader(Constants.USER_ID_HEADER) Long userId) {
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