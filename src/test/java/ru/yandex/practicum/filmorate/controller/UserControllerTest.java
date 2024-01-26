package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends FilmorateApplicationHandler {

    @Test
    void create_emptyBody_expect400() throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void create_LegalUser_expect200() throws IOException, InterruptedException {
        final String legalUserJson =
                "{ " +
                        "\"login\": \"Andrew\", " +
                        "\"name\": \"MyNick\"," +
                        "\"email\": \"email@tut.by\"," +
                        "\"birthday\": \"1970-02-01\"" +
                        "}";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(legalUserJson);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void update_ExistedUser_exp200() throws IOException, InterruptedException {
        final User updatedUser = User.builder()
                .id(1)
                .name("Andrew!")
                .login("AndrewLogin")
                .email("Andrew@Login.us")
                .birthday(LocalDate.EPOCH)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(updatedUser));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .PUT(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    /* -------------- VALIDATIONS --------------*/

    @Test
    void validate_IncorrectEmail_exp400() throws IOException, InterruptedException {
        final User updatedUser = User.builder()
                .login("AndrewLogin")
                .email("@Andrew@Login.us")
                .birthday(LocalDate.EPOCH)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(updatedUser));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_CorrectEmail_exp200() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("AndrewLogin")
                .email("This.is.correct@Login.us")
                .birthday(LocalDate.EPOCH)
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void validate_EmptyEmail_exp400() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("AndrewLogin")
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_NullLogin_exp400() throws IOException, InterruptedException {
        final User user = User.builder()
                .email("This.is.correct@Login.us")
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_EmptyLoginWithSpaces_exp400() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("")
                .email("This.is.correct@Login.us")
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void validate_NullName_expLogin() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("testLogin")
                .email("This.is.correct@Login.us")
                .birthday(LocalDate.EPOCH) //TODO: check serialization without date
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        User savedUser = GSON.fromJson(response.body(), User.class);
        String expectedName = user.getLogin();
        String actualName = savedUser.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    void validate_EmptyName_expLogin() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("testLogin")
                .name("")
                .email("This.is.correct@Login.us")
                .birthday(LocalDate.EPOCH) //TODO: check serialization without date
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        User savedUser = GSON.fromJson(response.body(), User.class);
        String expectedName = user.getLogin();
        String actualName = savedUser.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    void validate_FutureBirthDate_exp400() throws IOException, InterruptedException {
        final User user = User.builder()
                .login("testLogin")
                .name("Andrey")
                .email("This.is.correct@Login.us")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(GSON.toJson(user));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(RESOURCE_URI)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}