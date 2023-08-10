package ru.practicum.events.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventRequest;
import ru.practicum.events.model.EventStatus;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getRequestsForUserEvent(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("EventAdminController: get all events by Admin");
        return eventService.searchEventsByAdmin(
                users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("EventAdminController: update event id={} by Admin", eventId);
        return eventService.updateEventByAdmin(eventId, updateEventRequest);
    }
}
