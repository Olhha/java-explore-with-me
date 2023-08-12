package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class EventRequestStatusUpdateResult {
    List<RequestDto> confirmedRequests;
    List<RequestDto> rejectedRequests;
}
