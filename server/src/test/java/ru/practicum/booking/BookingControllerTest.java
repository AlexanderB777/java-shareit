package ru.practicum.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.booking.service.BookingService;
import ru.practicum.booking.status.BookingState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    private BookingController bookingController;
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        bookingService = mock(BookingService.class);
        bookingController = new BookingController(bookingService);
    }

    @Test
    public void create_shouldReturnBookingDtoResponse() {
        BookingDto bookingDto = new BookingDto();
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();

        when(bookingService.create(any(BookingDto.class), anyLong())).thenReturn(bookingDtoResponse);

        BookingDtoResponse result = bookingController.create(1L, bookingDto);

        assertNotNull(result);
        verify(bookingService, times(1)).create(any(BookingDto.class), anyLong());
    }

    @Test
    public void approve_shouldReturnUpdatedBookingDtoResponse() {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();

        when(bookingService.approve(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDtoResponse);

        BookingDtoResponse result = bookingController.approve(1L, 1L, true);

        assertNotNull(result);
        verify(bookingService, times(1)).approve(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    public void getBookingById_shouldReturnBookingDtoResponse() {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDtoResponse);

        BookingDtoResponse result = bookingController.getBookingById(1L, 1L);

        assertNotNull(result);
        verify(bookingService, times(1)).getBookingById(anyLong(), anyLong());
    }

    @Test
    public void getBookings_shouldReturnListOfBookingDtoResponses() {
        List<BookingDtoResponse> bookingDtoResponses = List.of(new BookingDtoResponse());

        when(bookingService.getBookingsByUserId(anyLong(), any(BookingState.class))).thenReturn(bookingDtoResponses);

        List<BookingDtoResponse> result = bookingController.getBookings(1L, BookingState.ALL);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingService, times(1)).getBookingsByUserId(anyLong(), any(BookingState.class));
    }
}
