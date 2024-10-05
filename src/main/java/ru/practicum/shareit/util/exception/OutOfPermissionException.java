package ru.practicum.shareit.util.exception;

public class OutOfPermissionException extends RuntimeException {
    public OutOfPermissionException(String str) {
        super(str);
    }
}
