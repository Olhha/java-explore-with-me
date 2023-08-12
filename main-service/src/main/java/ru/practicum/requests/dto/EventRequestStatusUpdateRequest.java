package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.requests.model.RequestStatus;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    RequestStatus status;
}
