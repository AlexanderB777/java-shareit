package ru.practicum.booking.service;

import ru.practicum.booking.status.BookingState;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse create(BookingDto bookingDto, long bookerId);

    BookingDtoResponse approve(Long bookingId, Long bookerId, Boolean approved);

    BookingDtoResponse getBookingById(long bookingId, long userId);

    List<BookingDtoResponse> getBookingsByUserId(long bookerId, BookingState state);
}