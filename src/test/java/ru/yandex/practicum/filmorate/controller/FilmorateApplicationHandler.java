package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.net.http.HttpClient;
import java.time.LocalDate;

@SpringBootTest
public abstract class FilmorateApplicationHandler {

    protected static final String SERVER_URL = "http://localhost:" + 8080;
    protected static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

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