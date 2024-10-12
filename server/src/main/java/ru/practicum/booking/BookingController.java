package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.booking.service.BookingService;
import ru.practicum.booking.status.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoResponse create(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                     @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable Long bookingId,
                                      @RequestHeader(USER_ID_HEADER) Long bookerId,
                                      @RequestParam Boolean approved) {
        return bookingService.approve(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getBookings(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                                @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByUserId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                                  @RequestParam BookingState state,
                                                                  @RequestHeader(USER_ID_HEADER)
                                                                  Long bookerId) {
        return null;
    }
}