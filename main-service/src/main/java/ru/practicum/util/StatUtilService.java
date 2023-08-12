package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.util.DateTimePattern.DATE_TIME_FORMATTER;

@Service
@Transactional
@RequiredArgsConstructor
public class StatUtilService {
    private final StatClient statClient;
    private static final String APP_NAME = "ewm-main-service";

    public Long getViewsForEvent(Long eventId) {
        Map<Long, Long> views = getViewsByEventIDs(List.of(eventId));

        if ((views != null) && (views.containsKey(eventId))) {
            return views.get(eventId);
        }
        return 0L;
    }

    public Map<Long, Long> getViewsByEventIDs(List<Long> eventIds) {
        return statClient.getViewsByEventIDs(eventIds);
    }

    public void postStats(String uri, String ip) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        statClient.postStats(APP_NAME, uri, ip, timestamp);
    }
}
