package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDate requestDate;
}
