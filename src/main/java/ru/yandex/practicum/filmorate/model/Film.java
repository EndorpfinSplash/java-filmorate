package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    public static final LocalDate OLDEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private int duration;

    private final Set<Integer> likes = new HashSet<>();
    private final Set<String> genres = new HashSet<>();

}
