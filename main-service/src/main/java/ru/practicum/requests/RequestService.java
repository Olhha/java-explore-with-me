package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.exception.CustomConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;
import ru.practicum.users.model.User;
import ru.practicum.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<RequestDto> getRequestsForUser(Long userId) {
        return RequestMapper.toRequestDtoList(requestRepository.findAllByRequesterId(userId));
    }

    public RequestDto addRequest(Long userId, Long eventId) {
        checkIfRequestExistsOrThrow(userId, eventId);

        Event event = getEventOrThrow(eventId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new CustomConflictException("Initiators can't add Requests for their own events.");
        }

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new CustomConflictException("Not permitted to add requests for UNPUBLISHED event.");
        }

        int limitParticipants = event.getParticipantLimit();

        if (limitParticipants > 0 && limitParticipants == getRequestsForEventQty(eventId)) {
            throw new CustomConflictException("Event participants limit has reached, limit="
                    + limitParticipants + ", confirmedRequests=" + getRequestsForEventQty(eventId));
        }

        RequestStatus status = RequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }

        Request request = Request.builder()
                .requester(getUserOrThrow(userId))
                .event(event)
                .created(LocalDateTime.now())
                .status(status)
                .build();

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Request with id = %d doesn't exist.", requestId)));

        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NotFoundException(String.format(
                    "Request with ID=%d not found for UserID=%d", requestId, userId));
        }
        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private Long getRequestsForEventQty(Long eventId) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    private void checkIfRequestExistsOrThrow(Long userId, Long eventId) {
        if (!requestRepository.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new CustomConflictException("Request for eventId=" + eventId
                    + "from userId=" + userId + " already exists.");
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "User with id = %d doesn't exist.", userId)));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Event with id = %d doesn't exist.", eventId)));
    }
}
