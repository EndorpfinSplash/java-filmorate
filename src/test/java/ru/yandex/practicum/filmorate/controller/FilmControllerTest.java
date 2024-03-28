package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest extends FilmorateApplicationHandler {

    private static final String ENDPOINT = "/films";
    private static final URI RESOURCE_URI = URI.create(SERVER_URL + ENDPOINT);
    @Autowired
    private ObjectMapper filmMapper;

    @Test
    void create_emptyBody_expect500() throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void create_LegalFilm_expect200() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .name("Matrix")
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    @Test
    void getAll_exp200() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }



    /* -------------- VALIDATIONS --------------*/

    @Test
    void validate_NullName_exp400() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_EmptyName_exp400() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .description("Good film!")
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_MaxDescription_exp400() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .name("Terminator")
                .description("t".repeat(201))
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_MaxDescription_exp200() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .name("Terminator")
                .description("t".repeat(200))
                .duration(120)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    @Test
    void validate_ReleaseDateOlder_exp400() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .name("Terminator")
                .description("cool film!")
                .duration(120)
                .releaseDate(LocalDate.of(1894, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_NegativeDuration_exp400() throws IOException, InterruptedException {
        final Film film = Film.builder()
                .name("Terminator")
                .description("cool film!")
                .duration(-120)
                .releaseDate(LocalDate.of(1894, 1, 1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(filmMapper.writeValueAsString(film));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

}