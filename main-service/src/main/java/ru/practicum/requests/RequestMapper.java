package ru.practicum.requests;

import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.model.Request;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.DateTimePattern.DATE_TIME_FORMATTER;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated() == null ? null : request.getCreated().format(DATE_TIME_FORMATTER))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<RequestDto> toRequestDtoList(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
