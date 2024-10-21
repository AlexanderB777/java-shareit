package ru.practicum.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.booking.mapper.BookingResponseMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.booking.status.BookingState;
import ru.practicum.booking.status.BookingStatus;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.ItemNotAvailableException;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.OutOfPermissionException;

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
    public BookingDtoResponse approve(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = getFromRepository(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), ownerId)) {
            throw new OutOfPermissionException("User not allowed to approve booking");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return responseMapper.map(repository.save(booking));
    }

    @Override
    public BookingDtoResponse getBookingById(long bookingId, long userId) {
        Booking booking = getFromRepository(bookingId);
        if (booking.getBooker().getId() == userId ||
                booking.getItem().getOwner().getId() == userId) {
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