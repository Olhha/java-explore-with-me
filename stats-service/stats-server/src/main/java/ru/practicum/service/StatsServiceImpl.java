package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.CustomValidationException;
import ru.practicum.mapper.HitsMapper;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.DateTimePattern.DATE_TIME_PATTERN;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public EndpointHitDto postStats(EndpointHitDto hitDto) {
        EndpointHit endpointHit = statsRepository.save(HitsMapper.toHit(hitDto));
        return HitsMapper.toDto(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(String startUnformatted, String endUnformatted,
                                       List<String> uris, boolean unique) {
        if (startUnformatted == null || endUnformatted == null) {
            throw new CustomValidationException("Start date and End date are required.");
        }
        LocalDateTime start = getParsedDate(startUnformatted);
        LocalDateTime end = getParsedDate(endUnformatted);

        checkDates(start, end);

        if (uris == null) {
            uris = List.of();
        }
        List<ViewStats> stats = List.of();

        if ((uris.size() == 0) && (!unique)) {
            stats = statsRepository.getStatsFull(start, end);
        }

        if ((uris.size() == 0) && (unique)) {
            stats = statsRepository.getStatsUniqueIP(start, end);
        }

        if ((uris.size() > 0) && (!unique)) {
            stats = statsRepository.getStatsWithUris(start, end, uris);
        }

        if ((uris.size() > 0) && (unique)) {
            stats = statsRepository.getStatsWithUrisUniqueIP(start, end, uris);
        }

        return stats.stream()
                .map(ViewStatsMapper::toDto)
                .collect(Collectors.toList());
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new CustomValidationException("Get Statistics request: End date should be after Start date");
        }
    }

    private static LocalDateTime getParsedDate(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return LocalDateTime.parse(date, format);
    }
}
