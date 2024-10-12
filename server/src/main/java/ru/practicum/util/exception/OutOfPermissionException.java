package ru.practicum.util.exception;

public class OutOfPermissionException extends RuntimeException {
    public OutOfPermissionException(String str) {
        super(str);
    }
}