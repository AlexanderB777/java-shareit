package ru.practicum.shareit.util.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String msg) {
        super(msg);
    }
}
