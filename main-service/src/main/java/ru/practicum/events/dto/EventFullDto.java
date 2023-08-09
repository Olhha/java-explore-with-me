package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventStatus;
import ru.practicum.users.dto.UserShortDto;

@Value
@Builder(toBuilder = true)
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    UserShortDto initiator;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    String publishedOn;
    Boolean requestModeration;
    EventStatus state;
    String title;
    Long views;
}
