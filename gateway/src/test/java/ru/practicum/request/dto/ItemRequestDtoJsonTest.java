package ru.practicum.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.ValidatorConfig;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(ValidatorConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoJsonTest {
    private final JacksonTester<ItemRequestDtoResponse> jacksonTester;

    private static final LocalDate THE_MOMENT = LocalDate.of(2024, 10, 1);
    private static final String THE_MOMENT_STRING = "2024-10-01";

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDtoResponse itemRequestDto = new ItemRequestDtoResponse();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(THE_MOMENT);

        JsonContent<ItemRequestDtoResponse> jsonContent = jacksonTester.write(itemRequestDto);

        assertThat(jsonContent)
                .extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created")
                .isEqualTo(THE_MOMENT_STRING);
    }
}