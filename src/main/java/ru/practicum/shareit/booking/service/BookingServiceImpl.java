package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingResponseMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.ItemNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.OutOfPermissionException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingResponseMapper responseMapper;

    @Override
    @Transactional
    public BookingDtoResponse create(BookingDto bookingDto, long bookerId) {
        Item item = itemRepository
                .findById(bookingDto
                        .getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        User user = userRepository
                .findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item not available");
        }
        return responseMapper.map(repository.save(
                new Booking(bookingDto.getStart(),
                        bookingDto.getEnd(),
                        item, user, BookingStatus.WAITING)));
    }

    @Override
    @Transactional
    public BookingDtoResponse approve(Long bookingId, Long bookerId, Boolean approved) {
        Booking booking = getFromRepository(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), bookerId)) {
            throw new OutOfPermissionException("User not allowed to approve booking");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return responseMapper.map(repository.save(booking));
    }

    @Override
    public BookingDtoResponse getBookingById(long bookingId, long userId) {
        Booking booking = getFromRepository(bookingId);
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            return responseMapper.map(booking);
        }
        return null;
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUserId(long bookerId, BookingState state) {
        return switch (state) {
            case ALL -> responseMapper.map(repository.findBookingsByBooker_IdOrderByStartDesc(bookerId));

            case CURRENT -> responseMapper.map(repository.findActiveBookingsByBookerId(bookerId));

            case PAST -> responseMapper.map(repository.findPastBookingsByBookerId(bookerId));

            case FUTURE -> responseMapper.map(repository.findFutureBookingsByBookerId(bookerId));

            case WAITING -> responseMapper
                    .map(repository
                            .findBookingsByBooker_IdAndStatusOrderByStartDesc(
                                    bookerId,
                                    BookingStatus.WAITING));

            case REJECTED -> responseMapper
                    .map(repository
                            .findBookingsByBooker_IdAndStatusOrderByStartDesc(
                                    bookerId,
                                    BookingStatus.REJECTED));

        };
    }

    private Booking getFromRepository(Long bookingId) {
        return repository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
    }
}