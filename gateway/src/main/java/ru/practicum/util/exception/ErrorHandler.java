package ru.practicum.util.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.InputStream;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(FeignResponseException.class)
    public ResponseEntity<ErrorResponse> feignResponseException(FeignResponseException e) {
        Response response = e.getResponse();

        int status = response.status();
        ErrorResponse errorResponse = extractErrorResponse(response);
        log.debug("Получена ошибка от Feign клиента: {}", errorResponse);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(final ValidationException e) {
        log.debug("Ошибка валидации: {}", e.getMessage());
        return new ErrorResponse("Ошибка ValidationException", e.getMessage());
    }

    private ErrorResponse extractErrorResponse(Response response) {
        try (InputStream bodyStream = response.body().asInputStream()) {
            return objectMapper.readValue(bodyStream, ErrorResponse.class);
        } catch (IOException ex) {
            return new ErrorResponse("Unknown error", response.toString());
        }
    }
}