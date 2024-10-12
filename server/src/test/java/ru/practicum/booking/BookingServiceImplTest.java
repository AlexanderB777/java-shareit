package ru.practicum.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.booking.mapper.BookingResponseMapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.repository.BookingRepository;
import ru.practicum.booking.service.BookingServiceImpl;
import ru.practicum.booking.status.BookingState;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.NotFoundException;
import ru.practicum.util.exception.OutOfPermissionException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private BookingResponseMapper bookingResponseMapper;

    @BeforeEach
    public void setUp() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        bookingResponseMapper = mock(BookingResponseMapper.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository, bookingResponseMapper);
    }

    @Test
    public void create_shouldThrowItemNotAvailableException_whenItemIsNotAvailable() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        Item item = new Item();
        item.setAvailable(false);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });

        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    public void approve_shouldThrowOutOfPermissionException_whenUserIsNotOwner() {
        Booking booking = new Booking();
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(OutOfPermissionException.class, () -> {
            bookingService.approve(1L, 2L, true);
        });

        verify(bookingRepository, times(0)).save(any(Booking.class));
    }

    @Test
    public void getBookingById_shouldReturnBooking_whenUserIsAllowed() {
        Booking booking = new Booking();
        User booker = new User();
        booker.setId(1L);
        Item item = new Item();
        item.setOwner(booker);

        booking.setBooker(booker);
        booking.setItem(item);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingResponseMapper.map(any(Booking.class))).thenReturn(new BookingDtoResponse());

        BookingDtoResponse response = bookingService.getBookingById(1L, 1L);

        assertNotNull(response);
        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookingsByUserId_shouldReturnAllBookingsForUser() {
        List<Booking> bookings = List.of(new Booking());

        when(bookingRepository.findBookingsByBooker_IdOrderByStartDesc(anyLong())).thenReturn(bookings);
        when(bookingResponseMapper.map(anyList())).thenReturn(List.of(new BookingDtoResponse()));

        List<BookingDtoResponse> responses = bookingService.getBookingsByUserId(1L, BookingState.ALL);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(bookingRepository, times(1)).findBookingsByBooker_IdOrderByStartDesc(1L);
    }
}
