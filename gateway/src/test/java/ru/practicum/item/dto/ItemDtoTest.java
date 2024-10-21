package ru.practicum.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.ValidatorConfig;
import ru.practicum.util.Marker;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(ValidatorConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> jacksonTester;
    private final Validator validator;

    private static final String NULL_VIOLATION = "{jakarta.validation.constraints.NotNull.message}";
    private static final String BLANK_VIOLATION = "{jakarta.validation.constraints.NotBlank.message}";

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        JsonContent<ItemDto> jsonContent = jacksonTester.write(itemDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(jsonContent)
                .extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
    }

    @Test
    void testItemDto_BlankName() throws Exception {
        String json = "{\"id\":1,\"name\":\" \",\"description\":\"description\",\"available\":true}";

        ItemDto itemDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Marker.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(BLANK_VIOLATION);
    }

    @Test
    void testItemDto_NullDescription() throws Exception {
        String json = "{\"id\":1,\"name\":\"name\",\"available\":true}";

        ItemDto itemDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Marker.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(BLANK_VIOLATION);
    }

    @Test
    void testItemDto_NullAvailable() throws Exception {
        String json = "{\"id\":1,\"name\":\"name\",\"description\":\"description\"}";

        ItemDto itemDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Marker.OnCreate.class);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(NULL_VIOLATION);
    }

    @Test
    void testItemDto_ThreeViolations() throws Exception {
        String json = "{\"id\":1,\"name\":\" \",\"description\":\" \"}";

        ItemDto itemDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Marker.OnCreate.class);

        assertThat(violations).hasSize(3);
    }
}