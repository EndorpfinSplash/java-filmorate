package ru.yandex.practicum.filmorate.exception;

public class ObjectAbsentException extends RuntimeException {
    public ObjectAbsentException(String s) {
        super(s);
    }
}