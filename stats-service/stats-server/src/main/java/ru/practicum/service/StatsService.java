package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;

import java.util.List;

@Service
public interface StatsService {
    EndpointHitDto postStats(EndpointHitDto request);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique);
}
