package ru.practicum.util.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String msg) {
        super(msg);
    }
}