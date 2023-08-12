package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.requests.model.RequestStatus;


@Value
@Builder(toBuilder = true)
public class RequestDto {
    Long id;
    String created;
    Long event;
    Long requester;
    RequestStatus status;
}
