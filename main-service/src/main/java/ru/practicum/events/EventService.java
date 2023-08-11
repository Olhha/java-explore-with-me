package ru.practicum.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.CategoryRepository;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.*;
import ru.practicum.exception.CustomConflictException;
import ru.practicum.exception.CustomValidationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.users.UserRepository;
import ru.practicum.util.StatUtilService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.util.DateTimePattern.DATE_TIME_FORMATTER;
import static ru.practicum.util.PageCreator.getPage;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatUtilService statUtilService;

    public List<EventShortDto> getAllEventsForOwner(Long userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findByInitiatorIdOrderByCreatedDesc(
                userId, getPage(from, size));

        setViewsAndConfirmedRequestsOnEvents(events);

        return EventMapper.toEventSortDtoList(events);
    }

    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        getEventDateOrThrow(newEventDto.getEventDate(), 2);

        Event event = EventMapper.newEventDtoToEvent(newEventDto);

        event.setInitiator(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "User with id = %d doesn't exist.", userId))));

        event.setCategory(getCategoryOrThrow(newEventDto.getCategory()));

        event.setCreated(LocalDateTime.now());

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }

        event.setStatus(EventStatus.PENDING);

        event.setViews(0L);
        event.setConfirmedRequests(0L);

        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        if (newEventDto.getParticipantLimit() == null || newEventDto.getParticipantLimit() <= 0) {
            event.setParticipantLimit(0);
        }

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto getEventByIdForOwner(Long userId, Long eventId) {
        Event event = getEventOrThrow(userId, eventId);
        setViewsAndConfirmedRequestsOnEvent(event);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto updateEventByOwner(Long userId, Long eventId,
                                           UpdateEventRequest updateEventRequest) {

        String eventDate = updateEventRequest.getEventDate();
        LocalDateTime newEventDate = getEventDateOrThrow(eventDate, 2);
        Event event = getEventOrThrow(userId, eventId);

        EventStatus status = event.getStatus();
        if (status != EventStatus.CANCELED && status != EventStatus.PENDING) {
            throw new CustomConflictException("Event can't be updated in state=" + status);
        }

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(getCategoryOrThrow(updateEventRequest.getCategory()));
        }

        EventMapper.toEventUpdated(event, updateEventRequest, newEventDate);

        String stateAction = updateEventRequest.getStateAction();
        if (stateAction != null) {
            EventStatus newStatus = parseUserStateActionOrThrow(stateAction);
            event.setStatus(newStatus);
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public List<RequestDto> getEventRequestsByOwner(Long userId, Long eventId) {
        getEventOrThrow(userId, eventId);

        return RequestMapper.toRequestDtoList(requestRepository.findAllByEventId(eventId));
    }

    public EventRequestStatusUpdateResult requestsStatusUpdateByOwner(
            Long userId, Long eventId, EventRequestStatusUpdateRequest requestsStatusUpdate) {
        Event event = getEventOrThrow(userId, eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new CustomValidationException("There is no need to confirm " +
                    "Requests for this EventId=" + eventId);
        }
        List<Request> requests = requestRepository.findAllByIdIn(requestsStatusUpdate.getRequestIds());

        List<Request> requestsNotPending = requests.stream()
                .filter(r -> r.getStatus() != RequestStatus.PENDING)
                .collect(Collectors.toList());

        if (!requestsNotPending.isEmpty()) {
            throw new CustomConflictException("Can't change status for not PENDING Requests.");
        }

        final List<Request> confirmedRequests;
        List<Request> rejectedRequests;

        if (RequestStatus.REJECTED.equals(requestsStatusUpdate.getStatus())) {
            confirmedRequests = List.of();
            rejectedRequests = requests.stream()
                    .peek(r -> r.setStatus(RequestStatus.REJECTED))
                    .collect(Collectors.toList());
        } else {

            long leftPlacesQty = event.getParticipantLimit() - getRequestsForEvent(eventId);
            if (leftPlacesQty == 0) {
                throw new CustomConflictException("There is no available places for the event.");
            }

            if (requests.size() > leftPlacesQty) {
                confirmedRequests = requests.stream()
                        .limit(leftPlacesQty)
                        .peek(r -> r.setStatus(RequestStatus.CONFIRMED))
                        .collect(Collectors.toList());

                rejectedRequests = requests.stream()
                        .filter(r -> !confirmedRequests.contains(r))
                        .peek(r -> r.setStatus(RequestStatus.REJECTED))
                        .collect(Collectors.toList());
            } else {
                confirmedRequests = requests.stream()
                        .peek(r -> r.setStatus(RequestStatus.CONFIRMED))
                        .collect(Collectors.toList());

                rejectedRequests = new ArrayList<>();
            }
        }

        if (!confirmedRequests.isEmpty()) {
            requestRepository.saveAll(confirmedRequests);
        }

        if (!rejectedRequests.isEmpty()) {
            requestRepository.saveAll(rejectedRequests);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(RequestMapper.toRequestDtoList(confirmedRequests))
                .rejectedRequests(RequestMapper.toRequestDtoList(rejectedRequests))
                .build();
    }

    public List<EventFullDto> searchEventsByAdmin(
            List<Long> users, List<EventStatus> states,
            List<Long> categories, String rangeStartUnformatted,
            String rangeEndUnformatted, Integer from, Integer size) {

        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;

        if (rangeStartUnformatted == null) {
            rangeStart = LocalDateTime.now().minusYears(100);
        } else {
            rangeStart = LocalDateTime.parse(rangeStartUnformatted, DATE_TIME_FORMATTER);
        }

        if (rangeEndUnformatted == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        } else {
            rangeEnd = LocalDateTime.parse(rangeEndUnformatted, DATE_TIME_FORMATTER);
        }

        List<Event> eventsFound = eventRepository.searchEventByAdmin(users, states,
                categories, rangeStart, rangeEnd, getPage(from, size));

        setViewsAndConfirmedRequestsOnEvents(eventsFound);

        return eventsFound.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public List<EventShortDto> searchEventsPublic(
            String text, List<Long> categories, Boolean paid,
            String rangeStartUnformatted, String rangeEndUnformatted,
            boolean onlyAvailable, EventSort sort, int from, int size,
            String clientIp, String uri) {

        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;

        if (rangeStartUnformatted == null) {
            rangeStart = LocalDateTime.now();
        } else {
            rangeStart = LocalDateTime.parse(rangeStartUnformatted, DATE_TIME_FORMATTER);
        }

        if (rangeEndUnformatted == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        } else {
            rangeEnd = LocalDateTime.parse(rangeEndUnformatted, DATE_TIME_FORMATTER);
        }

        if (rangeStart.isAfter(rangeEnd)) {
            throw new CustomValidationException("RangeStart can't be after rangeEnd");
        }

        statUtilService.postStats(uri, clientIp);

        EventStatus state = EventStatus.PUBLISHED;

        List<Event> events = eventRepository.searchEventPublic(text, categories, paid, state,
                rangeStart, rangeEnd, getPage(from, size));

        setViewsAndConfirmedRequestsOnEvents(events);

        List<Event> eventsFiltered = getEventsFilteredByAvailability(onlyAvailable, events);
        List<Event> eventsSorted = getEventsSortedByViews(sort, eventsFiltered);

        return EventMapper.toEventSortDtoList(eventsSorted);
    }

    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventRequest) {
        String eventDate = updateEventRequest.getEventDate();
        LocalDateTime newEventDate = getEventDateOrThrow(eventDate, 1);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("EventService: Event Not Found, eventId=" + eventId)
        );

        String stateAction = updateEventRequest.getStateAction();
        if (stateAction != null) {
            EventStatus newStatus = parseAdminStateActionOrThrow(stateAction);
            if (newStatus == EventStatus.PUBLISHED && event.getStatus() != EventStatus.PENDING) {
                throw new CustomConflictException("Event can't be published if it's not in PENDING state.");
            }
            if (newStatus == EventStatus.CANCELED && event.getStatus() == EventStatus.PUBLISHED) {
                throw new CustomConflictException("Event can't be cancelled if it's not in PUBLISHED state.");
            }
            event.setStatus(newStatus);
        }

        if (updateEventRequest.getCategory() != null) {
            event.setCategory(getCategoryOrThrow(updateEventRequest.getCategory()));
        }

        EventMapper.toEventUpdated(event, updateEventRequest, newEventDate);

        setViewsAndConfirmedRequestsOnEvent(event);
        event.setPublished(LocalDateTime.now());

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto getEventByIdPublic(Long eventId, String uri, String clientIp) {
        statUtilService.postStats(uri, clientIp);

        Event event = getPublishedEventByIdOrThrow(eventId);
        setViewsAndConfirmedRequestsOnEvent(event);
        return EventMapper.toEventFullDto(event);
    }

    private List<Event> getEventsSortedByViews(EventSort sort, List<Event> eventsFiltered) {
        if (EventSort.VIEWS.equals(sort)) {
            return eventsFiltered.stream()
                    .sorted(Comparator.comparing(Event::getViews))
                    .collect(Collectors.toList());
        }
        return eventsFiltered;
    }

    private List<Event> getEventsFilteredByAvailability(boolean onlyAvailable, List<Event> events) {
        if (onlyAvailable) {
            return events.stream()
                    .filter(e -> e.getParticipantLimit() == 0 || e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        return events;
    }


    private static EventStatus parseUserStateActionOrThrow(String stateAction) {
        EventStateOwnerAction statusParsed = EventStateOwnerAction.valueOf(stateAction);
        switch (statusParsed) {
            case SEND_TO_REVIEW:
            case CANCEL_REVIEW:
                return statusParsed.getEventStatus();
            default:
                throw new CustomConflictException("StateAction is not valid - " + stateAction);
        }
    }

    private static EventStatus parseAdminStateActionOrThrow(String stateAction) {
        EventStateAdminAction statusParsed = EventStateAdminAction.valueOf(stateAction);
        switch (statusParsed) {
            case PUBLISH_EVENT:
            case REJECT_EVENT:
                return statusParsed.getEventStatus();
            default:
                throw new CustomConflictException("StateAction is not valid - " + stateAction);
        }
    }

    private Category getCategoryOrThrow(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Category with id = %d doesn't exist.", catId)));
    }

    private LocalDateTime getEventDateOrThrow(String eventDateUnformatted, int numHours) {
        if (eventDateUnformatted == null) {
            return null;
        }
        LocalDateTime eventDate = LocalDateTime.parse(eventDateUnformatted, DATE_TIME_FORMATTER);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(numHours))) {
            throw new CustomValidationException("Event Date can't be earlier than " + numHours + " hours before now");
        }
        return eventDate;
    }

    private Event getEventOrThrow(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new NotFoundException("EventService: Event Not Found, eventId=" + eventId + " InitiatorId=" + userId);
        }
        return event;
    }

    private Event getPublishedEventByIdOrThrow(Long eventId) {
        Event event = eventRepository.findByIdAndStatus(eventId, EventStatus.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("EventService: Event Not Found, eventId=" + eventId);
        }
        return event;
    }

    private Long getRequestsForEvent(Long eventId) {
        Long requestsQty = requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        if (requestsQty == null) {
            return 0L;
        }
        return requestsQty;
    }

    private Map<Long, Long> getRequestsForEvents(List<Long> eventIds) {
        return requestRepository.findAllByStatusAndEventIdIn(
                RequestStatus.CONFIRMED, eventIds).stream().collect(Collectors.groupingBy(
                r -> r.getEvent().getId(), Collectors.counting()
        ));
    }

    private void setViewsAndConfirmedRequestsOnEvent(Event event) {
        Long eventId = event.getId();
        event.setViews(statUtilService.getViewsForEvent(eventId));
        event.setConfirmedRequests(getRequestsForEvent(eventId));
    }

    private void setViewsAndConfirmedRequestsOnEvents(List<Event> events) {
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<Long, Long> views = statUtilService.getViewsByEventIDs(eventIds);

        events.forEach(e -> e.setViews(Objects.requireNonNullElse(
                views.get(e.getId()), 0L)));

        Map<Long, Long> requestsEvents = getRequestsForEvents(eventIds);

        events.forEach(e -> e.setConfirmedRequests(Objects.requireNonNullElse(
                requestsEvents.get(e.getId()), 0L)));
    }
}
