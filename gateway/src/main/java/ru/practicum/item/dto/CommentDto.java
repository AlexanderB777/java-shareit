package ru.practicum.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotNull
    private String text;
    private String authorName;
    private Boolean created;
}