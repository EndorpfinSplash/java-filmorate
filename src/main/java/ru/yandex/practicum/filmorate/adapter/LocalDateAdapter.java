package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.value("");
            return;
        }
        jsonWriter.value(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        String localDateText = jsonReader.nextString();

        if (localDateText.isBlank()) {
            return null;
        }
        return LocalDate.parse(localDateText, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
