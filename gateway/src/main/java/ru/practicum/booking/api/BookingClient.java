package ru.practicum.booking.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.status.BookingState;
import ru.practicum.config.FeignClientConfig;

@FeignClient(url = "${shareit-server.url}",
        path = "/bookings",
        name = "bookingClient",
        configuration = FeignClientConfig.class)
public interface BookingClient {
    String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    ResponseEntity<?> createBooking(@RequestHeader(USER_ID_HEADER) long bookerId,
                                    @RequestBody BookingDto bookingDto);

    @PatchMapping("/{bookingId}")
    ResponseEntity<?> approveBooking(@PathVariable long bookingId,
                                     @RequestHeader(USER_ID_HEADER) long bookerId,
                                     @RequestParam boolean approved);

    @GetMapping("/{bookingId}")
    ResponseEntity<?> getBookingById(@PathVariable long bookingId,
                                     @RequestHeader(USER_ID_HEADER) long bookerId);

    @GetMapping
    ResponseEntity<?> getBookings(@RequestHeader(USER_ID_HEADER) long bookerId,
                                  @RequestParam BookingState state);

    @GetMapping("/owner")
    ResponseEntity<?> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                    @RequestParam BookingState state,
                                                    @RequestHeader(USER_ID_HEADER)
                                                    Long bookerId);
}