package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
public class UpdateEventRequest {
    @Size(min = 20, max = 2000, message = "UpdateEventRequest: annotation length should be from {min} to {max}")
    String annotation;
    Long category;

    @Size(min = 20, max = 7000, message = "UpdateEventRequest: description length should be from {min} to {max}")
    String description;

    String eventDate;
    LocationDto location;
    Boolean paid;

    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;

    @Size(min = 3, max = 120, message = "UpdateEventRequest: title length should be from {min} to {max}")
    String title;
}
