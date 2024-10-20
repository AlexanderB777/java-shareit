package ru.practicum.booking.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.status.BookingState;
import ru.practicum.util.Constants;
import ru.practicum.util.config.FeignClientConfig;

@FeignClient(url = "${shareit-server.url}",
        path = "/bookings",
        name = "bookingClient",
        configuration = FeignClientConfig.class)
public interface BookingClient {

    @PostMapping
    ResponseEntity<?> createBooking(@RequestHeader(Constants.USER_ID_HEADER) long bookerId,
                                    @RequestBody BookingDto bookingDto);

    @PatchMapping("/{bookingId}")
    ResponseEntity<?> approveBooking(@PathVariable long bookingId,
                                     @RequestHeader(Constants.USER_ID_HEADER) long bookerId,
                                     @RequestParam boolean approved);

    @GetMapping("/{bookingId}")
    ResponseEntity<?> getBookingById(@PathVariable long bookingId,
                                     @RequestHeader(Constants.USER_ID_HEADER) long bookerId);

    @GetMapping
    ResponseEntity<?> getBookings(@RequestHeader(Constants.USER_ID_HEADER) long bookerId,
                                  @RequestParam BookingState state);

    @GetMapping("/owner")
    ResponseEntity<?> getBookingsFromItemsByOwnerId(@PathVariable long ownerId,
                                                    @RequestParam BookingState state,
                                                    @RequestHeader(Constants.USER_ID_HEADER)
                                                    Long bookerId);
}