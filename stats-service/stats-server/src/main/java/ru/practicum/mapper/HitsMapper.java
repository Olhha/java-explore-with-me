package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.DateTimePattern.DATE_TIME_FORMAT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitsMapper {
    public static EndpointHit toHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(),
                        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .build();
    }

    public static EndpointHitDto toDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp().toString())
                .ip(endpointHit.getIp())
                .build();
    }
}
