package ru.practicum.shareit.util;

public class OutOfPermissionException extends RuntimeException {
    public OutOfPermissionException(String str) {
        super(str);
    }
}
