package ru.yandex.practicum.filmorate.exception;

public class UserAbsentException extends RuntimeException {
    public UserAbsentException(String s) {
        super(s);
    }
}