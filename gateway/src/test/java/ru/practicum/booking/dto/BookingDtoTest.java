package ru.practicum.booking.dto;


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

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(ValidatorConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> jacksonTester;
    private final Validator validator;

    private static final LocalDateTime START_TIME = LocalDateTime
            .of(2020, 1, 1, 0, 0);
    private static final String START_TIME_STRING = "2020-01-01T00:00:00";
    private static final LocalDateTime END_TIME = LocalDateTime
            .of(2021, 1, 1, 0, 0, 0);
    private static final String END_TIME_STRING = "2021-01-01T00:00:00";
    private static final String NULL_VIOLATION = "{jakarta.validation.constraints.NotNull.message}";

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(START_TIME);
        bookingDto.setEnd(END_TIME);
        bookingDto.setItemId(2L);

        JsonContent<BookingDto> jsonContent = jacksonTester.write(bookingDto);

        assertThat(jsonContent).isNotNull();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo(START_TIME_STRING);
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo(END_TIME_STRING);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
    }

    @Test
    void testBookingDto_StartNull() throws Exception {
        String json = "{\"id\":1,\"end\":\"2024-10-17T14:30:00\",\"itemId\":2}";

        BookingDto bookingDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(NULL_VIOLATION);
    }

    @Test
    void testBookingDto_EndNull() throws Exception {
        String json = "{\"id\":1,\"start\":\"2024-10-17T12:30:00\",\"itemId\":2}";

        BookingDto bookingDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(NULL_VIOLATION);
    }

    @Test
    void testBookingDto_ItemIdNull() throws Exception {
        String json = "{\"id\":1,\"start\":\"2024-10-17T12:30:00\",\"end\":\"2024-10-17T14:30:00\"}";

        BookingDto bookingDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessageTemplate()).contains(NULL_VIOLATION);
    }

    @Test
    void testBookingDto_TwoViolations() throws Exception {
        String json = "{\"itemId\":2}";

        BookingDto bookingDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

        assertThat(violations).hasSize(2);
    }
}