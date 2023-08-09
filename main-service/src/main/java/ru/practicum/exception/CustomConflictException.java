package ru.practicum.exception;

public class CustomConflictException extends RuntimeException {
    public CustomConflictException(String s) {
        super(s);
    }
}
