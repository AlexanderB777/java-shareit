package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto dto,
                                 @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getPersonalRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return service.getPersonalRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return service.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId) {
        return service.getById(requestId);
    }
}