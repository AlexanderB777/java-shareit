package ru.practicum.booking.dto;

import lombok.Data;
import ru.practicum.booking.status.BookingStatus;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingDtoResponse {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;
}
