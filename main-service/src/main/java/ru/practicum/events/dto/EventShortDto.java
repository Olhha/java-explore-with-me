package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class EventShortDto {
    int id;

    @NotBlank(message = "EventFullDto: annotation can't be empty")
    String annotation;

    @NotNull(message = "EventFullDto: category can't be null")
    CategoryDto category;
    long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    UserShortDto initiator;
    boolean paid;
    String title;
    long views;
}
