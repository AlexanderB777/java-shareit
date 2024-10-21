package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.mapper.CommentMapper;
import ru.practicum.item.model.Comment;
import ru.practicum.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    public void toDto_shouldMapEntityToDto() {
        User author = new User();
        author.setName("Test User");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setAuthor(author);

        CommentDto commentDto = commentMapper.toDto(comment);

        assertNotNull(commentDto);
        assertEquals(1L, commentDto.getId());
        assertEquals("Test Comment", commentDto.getText());
        assertEquals("Test User", commentDto.getAuthorName());
    }
}