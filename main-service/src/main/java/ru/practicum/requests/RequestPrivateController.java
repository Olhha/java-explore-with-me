package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping()
    public List<RequestDto> getAllEvents(@PathVariable Long userId) {
        log.info("RequestPrivateController: get all requests from user id={}", userId);
        return requestService.getRequestsForUser(userId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam Long eventId) {
        log.info("RequestPrivateController: Creating new request for eventId={} for userId={}", eventId, userId);
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("RequestPrivateController: cancel request with ID = {} from userId={}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }

}
