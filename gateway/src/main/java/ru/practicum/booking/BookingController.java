package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.api.BookingClient;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.status.BookingState;

import static ru.practicum.util.Constants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        return client.createBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approve(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID_HEADER) Long bookerId,
                                     @RequestParam Boolean approved) {
        return client.approveBooking(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable Long bookingId,
                                            @RequestHeader(USER_ID_HEADER) Long userId) {
        return client.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<?> getBookings(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        return client.getBookings(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                           @RequestParam BookingState state,
                                                           @RequestHeader(USER_ID_HEADER)
                                                           Long bookerId) {
        return client.getBookingsFromItemsByOwnerId(ownerId, state, bookerId);
    }
}