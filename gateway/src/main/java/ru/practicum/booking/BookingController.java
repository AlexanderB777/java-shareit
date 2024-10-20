package ru.practicum.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.api.BookingClient;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.status.BookingState;
import ru.practicum.util.Constants;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(Constants.USER_ID_HEADER) Long bookerId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        return client.createBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approve(@PathVariable Long bookingId,
                                     @RequestHeader(Constants.USER_ID_HEADER) Long bookerId,
                                     @RequestParam @NotNull Boolean approved) {
        return client.approveBooking(bookingId, bookerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable Long bookingId,
                                            @RequestHeader(Constants.USER_ID_HEADER) Long userId) {
        return client.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<?> getBookings(@RequestHeader(Constants.USER_ID_HEADER) Long bookerId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        return client.getBookings(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                           @RequestParam BookingState state,
                                                           @RequestHeader(Constants.USER_ID_HEADER)
                                                           @NotNull Long bookerId) {
        return client.getBookingsFromItemsByOwnerId(ownerId, state, bookerId);
    }
}