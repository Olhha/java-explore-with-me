package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewStatsMapper {
    public static ViewStatsDto toDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
