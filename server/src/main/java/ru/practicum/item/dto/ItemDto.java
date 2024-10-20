package ru.practicum.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}