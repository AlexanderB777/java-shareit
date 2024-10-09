package ru.practicum.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingResponseMapper {
    BookingDtoResponse map(Booking booking);

    List<BookingDtoResponse> map(List<Booking> bookings);
}
