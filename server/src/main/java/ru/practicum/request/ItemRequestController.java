package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDtoResponse create(@RequestBody ItemRequestDto dto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        return service.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoResponse> getPersonalRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return service.getPersonalRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> getAllRequests() {
        return service.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse getRequestById(@PathVariable Long requestId) {
        return service.getById(requestId);
    }
}