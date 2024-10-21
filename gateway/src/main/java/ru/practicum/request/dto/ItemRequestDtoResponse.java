package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.item.dto.ItemDto;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDtoResponse {
    private Long id;
    private String description;
    private LocalDate created;
    private List<ItemDto> items;
}