package ru.practicum.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.booking.service.BookingServiceImpl;
import ru.practicum.booking.status.BookingState;
import ru.practicum.booking.status.BookingStatus;
import ru.practicum.util.exception.ItemNotAvailableException;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.OutOfPermissionException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Sql(statements = {
        "INSERT INTO users VALUES ( 1, 'Alex', 'alex@gmail.com' )",
        "INSERT INTO users VALUES ( 2, 'Andrew', 'andrew@gmail.com' )",
        "INSERT INTO users VALUES ( 3, 'John', 'john@gmail.com' )",
        "INSERT INTO items (id, name, description, available, owner_id) " +
                "VALUES ( 1, 'thing', 'thing description', TRUE, 1)",
        "INSERT INTO items (id, name, description, available, owner_id) " +
                "VALUES ( 2, 'another thing', 'another thing description', TRUE, 2 )",
        "INSERT INTO items (id, name, description, available, owner_id) " +
                "VALUES ( 3, 'unavailable thing', 'unavailable thing description', FALSE, 1 )"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(statements = {
        "DELETE FROM items",
        "DELETE FROM users"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BookingServiceImplTest {
    @Autowired
    private BookingServiceImpl bookingService;
    @SpyBean
    private BookingRepository bookingRepository;

    private static final LocalDateTime START_TIME =
            LocalDateTime.of(2024, 11, 1, 10, 0);
    private static final String START_TIME_H2 = "TIMESTAMP '2024-11-01 10:00:00'";
    private static final LocalDateTime END_TIME =
            LocalDateTime.of(2024, 11, 2, 10, 0);
    private static final String END_TIME_H2 = "TIMESTAMP '2024-11-02 10:00:00'";

    @Test
    @Transactional
    @DisplayName("Создание нового бронирования")
    void testCreate() {
        // given
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(START_TIME);
        bookingDto.setEnd(END_TIME);
        bookingDto.setItemId(1L);

        // when
        BookingDtoResponse bookingDtoResponse = bookingService.create(bookingDto, 2L);

        // then
        assertEquals(1L, bookingDtoResponse.getId());
        assertEquals(START_TIME, bookingDtoResponse.getStart());
        assertEquals(END_TIME, bookingDtoResponse.getEnd());
        assertEquals(BookingStatus.WAITING, bookingDtoResponse.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Создание нового бронирования на недоступную вещь")
    void testCreate_UnavailableItem() {
        // given
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(START_TIME);
        bookingDto.setEnd(END_TIME);
        bookingDto.setItemId(3L);

        // when & then
        Exception exception = assertThrows(ItemNotAvailableException.class,
                () -> bookingService.create(bookingDto, 1L));
        assertEquals("Item not available", exception.getMessage());
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Создание нового бронирования несуществующей вещи")
    void testCreate_NotExistingItem() {
        // given
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(START_TIME);
        bookingDto.setEnd(END_TIME);
        bookingDto.setItemId(500L);

        // when & then
        Exception exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(bookingDto, 1L));
        assertEquals("Item not found", exception.getMessage());
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Сздание нового бронирования несуществующий пользователем")
    void testCreate_NotExistingUser() {
        // given
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(START_TIME);
        bookingDto.setEnd(END_TIME);
        bookingDto.setItemId(2L);

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(bookingDto, 500L));
        assertEquals("User not found", exception.getMessage());
        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "1, " + // item ID
            "2, " + // booker ID
            "'WAITING')")
    @DisplayName("Подверждение существующего бронирования")
    void testApprove() {
        // when
        BookingDtoResponse bookingDtoResponse = bookingService.approve(1L, 1L, true);

        // then
        assertEquals(BookingStatus.APPROVED, bookingDtoResponse.getStatus());
        assertEquals(START_TIME, bookingDtoResponse.getStart());
        assertEquals(END_TIME, bookingDtoResponse.getEnd());
        assertEquals(2L, bookingDtoResponse.getBooker().getId());
        assertEquals(1L, bookingDtoResponse.getItem().getId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Подтверждение несуществующего бронирования")
    void testApprove_NotExistingBooking() {
        // when & then
        Exception exception = assertThrows(NotFoundException.class,
                () -> bookingService.approve(500L, 1L, true));
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "1, " + // item ID
            "2, " + // booker ID
            "'WAITING')")
    @DisplayName("Отклонение бронирования")
    void testApprove_RejectBooking() {
        // when
        BookingDtoResponse bookingDtoResponse = bookingService.approve(1L, 1L, false);

        // then
        assertEquals(BookingStatus.REJECTED, bookingDtoResponse.getStatus());
        assertEquals(START_TIME, bookingDtoResponse.getStart());
        assertEquals(END_TIME, bookingDtoResponse.getEnd());
        assertEquals(2L, bookingDtoResponse.getBooker().getId());
        assertEquals(1L, bookingDtoResponse.getItem().getId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "1, " + // item ID
            "2, " + // booker ID
            "'WAITING')")
    @DisplayName("Подтверждение бронирования чужим пользователем")
    void testApprove_UserNotAllowed() {
        // when & then
        Exception exception = assertThrows(OutOfPermissionException.class,
                () -> bookingService.approve(1L, 3L, true));
        assertEquals("User not allowed to approve booking", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "1, " + // item ID
            "2, " + // booker ID
            "'WAITING')")
    @DisplayName("Получения бронирования хозяином бронирования")
    void testGetBookingById() {
        // when
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(1L, 2L);

        // then
        assertEquals(1L, bookingDtoResponse.getId());
        assertEquals(1L, bookingDtoResponse.getItem().getId());
        assertEquals(2L, bookingDtoResponse.getBooker().getId());
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "2, " + // item ID
            "1, " + // booker ID
            "'WAITING')")
    @DisplayName("Получения бронирования хозяином вещи")
    void testGetBookingById_ItemOwner() {
        // when
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(1L, 2L);

        // then
        assertEquals(1L, bookingDtoResponse.getId());
        assertEquals(2L, bookingDtoResponse.getItem().getId());
        assertEquals(1L, bookingDtoResponse.getBooker().getId());
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    @Transactional
    @Sql(statements = "INSERT INTO bookings VALUES ( " +
            "1, " +
            START_TIME_H2 + ", " +
            END_TIME_H2 + ", " +
            "2, " + // item ID
            "2, " + // booker ID
            "'WAITING')")
    @DisplayName("Получения бронирования другим пользователем")
    void testGetBookingById_AnotherUser() {
        // when
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(1L, 3L);

        // then
        assertNull(bookingDtoResponse);
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO bookings VALUES ( " +
                    "1, " +
                    START_TIME_H2 + ", " +
                    END_TIME_H2 + ", " +
                    "1, " + // item ID
                    "3, " + // booker ID
                    "'WAITING')",

            "INSERT INTO bookings VALUES ( " +
                    "2, " +
                    START_TIME_H2 + ", " +
                    END_TIME_H2 + ", " +
                    "2, " + // item ID
                    "3, " + // booker ID
                    "'WAITING')"
    })
    @DisplayName("Получение списка всех бронирований пользователя")
    void testGetBookingsByUserId() {
        // when
        List<BookingDtoResponse> bookings = bookingService.getBookingsByUserId(3L, BookingState.ALL);

        // then
        assertEquals(2, bookings.size());
        verify(bookingRepository, times(1)).findBookingsByBooker_IdOrderByStartDesc(anyLong());
    }

    @Test
    @Transactional
    @Sql(statements = {
            "INSERT INTO bookings VALUES ( " +
                    "1, " +
                    START_TIME_H2 + ", " +
                    END_TIME_H2 + ", " +
                    "1, " + // item ID
                    "3, " + // booker ID
                    "'WAITING')",

            "INSERT INTO bookings VALUES ( " +
                    "2, " +
                    "TIMESTAMP '2024-10-01 10:00:00', " +
                    END_TIME_H2 + ", " +
                    "2, " + // item ID
                    "3, " + // booker ID
                    "'APPROVED')"
    })
    @DisplayName("Получение списка текущих бронирований пользователя")
    void testGetBookingsByUserId_CurrentBookings() {
        // when
        List<BookingDtoResponse> bookings = bookingService.getBookingsByUserId(3L, BookingState.CURRENT);

        // then
        assertEquals(1, bookings.size());
        verify(bookingRepository, times(1)).findActiveBookingsByBookerId(anyLong());
    }
}