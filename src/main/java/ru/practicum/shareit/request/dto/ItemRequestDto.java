package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    @Positive
    private Long requestor;
    @NotNull
    private LocalDate requestDate;
}
