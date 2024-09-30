package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.status.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse create(BookingDto bookingDto, long bookerId);

    BookingDtoResponse approve(Long bookingId, Long bookerId, Boolean approved);

    BookingDtoResponse getBookingById(long bookingId, long userId);

    List<BookingDtoResponse> getBookingsByUserId(long bookerId, BookingState state);
}
