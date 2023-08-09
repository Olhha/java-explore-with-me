package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "NewEventDto: annotation length should be from {min} to {max}")
    String annotation;

    @NotNull(message = "NewEventDto: category can't be null")
    Long category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "NewEventDto: description length should be from {min} to {max}")
    String description;

    @NotNull(message = "NewEventDto: eventDate can't be null")
    String eventDate;

    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;

    @NotBlank
    @Size(min = 3, max = 120, message = "NewEventDto: title length should be from {min} to {max}.")
    String title;
}