package ru.yandex.practicum.filmorate.exception;

public class FilmAbsentException extends RuntimeException {
    public FilmAbsentException(String s) {
        super(s);
    }
}