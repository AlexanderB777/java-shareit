package ru.practicum.util.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.InputStream;

@RestControllerAdvice
public class ErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(FeignResponseException.class)
    public ResponseEntity<ErrorResponse> feignResponseException(FeignResponseException e) {
        Response response = e.getResponse();

        int status = response.status();
        ErrorResponse errorResponse = extractErrorResponse(response);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private ErrorResponse extractErrorResponse(Response response) {
        try (InputStream bodyStream = response.body().asInputStream()) {
            return objectMapper.readValue(bodyStream, ErrorResponse.class);
        } catch (IOException ex) {
            return new ErrorResponse("Unknown error", response.toString());
        }
    }
}