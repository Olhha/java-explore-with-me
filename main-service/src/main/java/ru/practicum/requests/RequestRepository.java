package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterIdAndEventId(long requesterId, long eventId);

    Long countAllByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByStatusAndEventIdIn(RequestStatus requestStatus, List<Long> eventIds);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByEventId(Long eventId);

}
