package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventStateAdminAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class UpdateEventRequest {
    @Size(min = 20, max = 2000, message = "UpdateEventRequest: annotation length should be from {min} to {max}")
    String annotation;
    CategoryDto category;

    @Size(min = 20, max = 7000, message = "UpdateEventRequest: description length should be from {min} to {max}")
    String description;

    @Future(message = "NewEventDto: eventDate should be later than now")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationDto location;
    boolean paid;
    int participantLimit;
    boolean requestModeration;
    EventStateAdminAction stateAction;

    @Size(min = 3, max = 120, message = "UpdateEventRequest: title length should be from {min} to {max}")
    String title;
}
