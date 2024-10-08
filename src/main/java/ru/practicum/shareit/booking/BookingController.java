package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoResponse create(@RequestHeader(USER_ID_HEADER) @NotNull Long bookerId,
                                     @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable Long bookingId,
                        @RequestHeader(USER_ID_HEADER) @NotNull Long bookerId,
                        @RequestParam @NotNull Boolean approved) {
        return bookingService.approve(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getBookings(@RequestHeader(USER_ID_HEADER) @NotNull Long bookerId,
                                                @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByUserId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                                  @RequestParam BookingState state,
                                                                  @RequestHeader(USER_ID_HEADER)
                                                                      @NotNull Long bookerId) {
        return null;
    }
}