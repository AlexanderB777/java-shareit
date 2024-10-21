package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.request.repository.ItemRequestRepository;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;

    @Override
    public ItemRequestDtoResponse create(ItemRequestDto itemRequestDto, Long requesterId) {
        ItemRequest itemRequest = mapper.toEntity(itemRequestDto);
        itemRequest.setCreated(LocalDate.now());
        itemRequest.setRequestor(userRepository
                .findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User not found")));
        return mapper.toDto(repository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoResponse> getPersonalRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        return mapper.toDto(repository.findByRequestor_Id(userId));
    }

    @Override
    public List<ItemRequestDtoResponse> getAllRequests() {
        return mapper.toDto(repository.findAllByOrderByCreatedDesc());
    }

    @Override
    public ItemRequestDtoResponse getById(Long id) {
        ItemRequest itemRequest = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        return mapper.toDto(itemRequest);
    }
}