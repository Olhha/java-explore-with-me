package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;


@Value
@Builder(toBuilder = true)
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    String eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
}
