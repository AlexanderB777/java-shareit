package ru.practicum.util.exception;

import feign.Response;
import lombok.Getter;

@Getter
public class FeignResponseException extends RuntimeException {
    private final Response response;

    public FeignResponseException(Response response) {
        super("Received error response: " + response.status());
        this.response = response;
    }
}