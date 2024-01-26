package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public abstract class FilmorateApplicationHandler {

    private static final String SERVER_URL = "http://localhost:" + 8080;
    private static final String USERS_ENDPOINT = "/users";
    protected static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    protected static final URI RESOURCE_URI = URI.create(SERVER_URL + USERS_ENDPOINT);
    protected static final Gson GSON =new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    static ConfigurableApplicationContext applicationContext;


    @BeforeAll
    static void setUp() {
         applicationContext = SpringApplication.run(FilmorateApplication.class);
    }

    @AfterAll
    static void tearDown() {
        SpringApplication.exit(applicationContext, () -> 0);
    }

}