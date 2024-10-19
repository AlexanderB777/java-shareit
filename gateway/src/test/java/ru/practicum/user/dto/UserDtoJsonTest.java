package ru.practicum.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(ValidatorConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoJsonTest {
    private final JacksonTester<UserDto> jacksonTester;
    private final Validator validator;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Alex");
        userDto.setEmail("alex@gmail.com");

        JsonContent<UserDto> jsonContent = jacksonTester.write(userDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Alex");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("alex@gmail.com");
    }

    @Test
    void testUserDto_IncorrectEmail() throws Exception {
        String json = "{\"id\": 1, \"name\": \"Alex\", \"email\": \"alexgmail.com\"}";

        UserDto userDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("должно иметь формат адреса электронной почты");
    }

    @Test
    void testUserDto_IncorrectName() throws Exception {
        String json = "{\"id\": 1, \"name\": \" \", \"email\": \"alex@gmail.com\"}";

        UserDto userDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("не должно быть пустым");
    }

    @Test
    void testUserDto_TwoViolations() throws Exception {
        String json = "{\"id\": 1, \"name\": \" \", \"email\": \"alexgmail.com\"}";

        UserDto userDto = jacksonTester.parseObject(json);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertThat(violations).hasSize(2);
    }
}