package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.request.dto.ItemRequestDto;
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
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long requesterId) {
        itemRequestDto.setCreated(LocalDate.now());
        ItemRequest itemRequest = mapper.toEntity(itemRequestDto);
        itemRequest.setRequestor(userRepository
                .findById(requesterId)
                .orElseThrow(() -> new NotFoundException("User not found")));
        return mapper.toDto(repository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getPersonalRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        return mapper.toDto(repository.findByRequestor_Id(userId));
    }

    @Override
    public List<ItemRequestDto> getAllRequests() {
        return mapper.toDto(repository.findAllByOrderByCreatedDesc());
    }

    @Override
    public ItemRequestDto getById(Long id) {
        ItemRequest itemRequest = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        return mapper.toDto(itemRequest);
    }
}