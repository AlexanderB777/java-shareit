package ru.practicum.item.dto;

import lombok.Data;
import ru.practicum.booking.dto.BookingDtoResponse;

import java.util.List;

@Data
public class ItemDtoResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoResponse lastBooking;
    private BookingDtoResponse nextBooking;
    private List<String> comments;
    private Long requestId;
}
