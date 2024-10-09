package ru.practicum.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(target = "created", defaultValue = "true")
    CommentDto toDto(Comment comment);
}
