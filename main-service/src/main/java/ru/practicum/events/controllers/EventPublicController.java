package ru.practicum.events.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
public class EventPublicController {
    private final EventService eventService;

    @Autowired
    public EventPublicController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public List<EventShortDto> searchEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("EventPublicController: get all events");

        return eventService.searchEventsPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size,
                request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdPublic(
            @PathVariable Long eventId, HttpServletRequest request) {
        log.info("EventPublicController: update event id={}", eventId);
        return eventService.getEventByIdPublic(eventId, request.getRequestURI(),
                request.getRemoteAddr());
    }
}
