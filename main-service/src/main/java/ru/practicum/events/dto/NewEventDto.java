package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.categories.dto.CategoryDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class NewEventDto {
    @Size(min = 20, max = 2000, message = "NewEventDto: annotation length should be from {min} to {max}")
    String annotation;

    @NotNull(message = "NewEventDto: category can't be null")
    CategoryDto category;

    @Size(min = 20, max = 7000, message = "NewEventDto: description length should be from {min} to {max}")
    String description;

    @Future(message = "NewEventDto: eventDate should be later than now")
    @NotNull(message = "NewEventDto: eventDate can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationDto location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;

    @Size(min = 3, max = 120, message = "NewEventDto: title length should be from {min} to {max}.")
    String title;
}
