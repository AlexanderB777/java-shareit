package ru.practicum.item.dto;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.user.dto.ValidatorConfig;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(ValidatorConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private final JacksonTester<CommentDto> jacksonTester;
    private final Validator validator;

    @Test
    void testCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("This is a comment");

        JsonContent<CommentDto> jsonContent = jacksonTester.write(commentDto);

        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.text")
                .isEqualTo("This is a comment");
    }
}