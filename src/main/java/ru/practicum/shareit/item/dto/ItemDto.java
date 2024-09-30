package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private BookingDtoResponse lastBooking;
    private BookingDtoResponse nextBooking;
    private List<String> comments;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ItemDto(");
        if (id != null) {
            builder.append("id=").append(id).append(", ");
        }
        if (name != null) {
            builder.append("name=").append(name).append(", ");
        }
        if (description != null) {
            builder.append("descrL=").append(description.length()).append(", ");
        }
        if (available != null) {
            builder.append("available=").append(available).append(", ");
        }
        if (lastBooking != null) {
            builder.append("lastBooking=").append(lastBooking).append(", ");
        }
        if (nextBooking != null) {
            builder.append("nextBooking=").append(nextBooking).append(", ");
        }
        if (comments != null) {
            builder.append("comments=").append(comments).append(", ");
        }
        int toDelete = builder.lastIndexOf(", ");
        if (toDelete != -1) {
            builder.delete(toDelete, builder.length());
        }
        builder.append(")");
        return builder.toString();
    }
}
