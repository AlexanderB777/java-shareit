package ru.practicum.item.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private Boolean created;
}