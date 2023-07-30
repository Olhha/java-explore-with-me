package ru.practicum.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;

import java.util.List;

@Service
@Transactional
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventShortDto> getAllEvents(Long userId, Integer from, Integer size) {
        return null;
    }

    public NewEventDto addEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.newEventToEvent(newEventDto);
        eventRepository.save(event);
        return null;
    }

    public EventFullDto getEventById(Long userId, Long eventId) {
        return null;
    }

    public List<EventShortDto> updateEvent(Long userId, Long eventId) {
        return null;
    }
}
