package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.item.dto.ItemDto;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDto {
    private Long id;
    @NotNull
    private String description;
    private LocalDate created;
    private List<ItemDto> items;
}