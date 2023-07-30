package ru.practicum.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.categories.CategoryMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.users.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static Event newEventToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(CategoryMapper.toCategory(newEventDto.getCategory()))
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreated())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(new LocationDto(event.getLat(),event.getLon()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublished())
                .requestModeration(event.isRequestModeration())
                .state(event.getStatus())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
