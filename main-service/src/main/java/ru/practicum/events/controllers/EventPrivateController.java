package ru.practicum.events.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
public class EventPrivateController {
    private final EventService eventService;

    @Autowired
    public EventPrivateController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("EventPrivateController: get all events from = {} size = {} for user id = {}", from, size, userId);
        return eventService.getAllEventsForOwner(userId, from, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        log.info("EventPrivateController: Creating event {}", newEventDto);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("EventPrivateController: get full event info by ID = {} for userId={}", eventId, userId);
        return eventService.getEventByIdForOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("EventPrivateController: update event with ID = {} by userId={}", eventId, userId);
        return eventService.updateEventByOwner(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsForEvent(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("EventPrivateController: get all requests for event with ID = {} for userId={}",
                eventId, userId);
        return eventService.getEventRequestsByOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult requestsStatusUpdate(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest requestsStatusUpdate) {
        log.info("EventPrivateController: update Requests status by userId={} for eventId={}", userId, eventId);
        return eventService.requestsStatusUpdateByOwner(userId, eventId, requestsStatusUpdate);
    }
}
