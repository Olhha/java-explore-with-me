package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.util.DateTimePattern.DATE_TIME_PATTERN;


@Service
@Slf4j
public class StatClient {
    private final String serverURL;
    private final RestTemplate restTemplate;

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverURL) {
        this.serverURL = serverURL;
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public ResponseEntity<Object> postStats(String app, String uri, String ip, String timestamp) {

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        HttpEntity<EndpointHitDto> entity = new HttpEntity<>(endpointHitDto, defaultHeaders());

        return this.restTemplate.postForEntity(
                serverURL + "/hit",
                entity,
                Object.class);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        params.put("end", end.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        params.put("uris", String.join(",", uris));
        params.put("isUnique", unique);

        HttpEntity<Object> request = new HttpEntity<>(defaultHeaders());

        return restTemplate.exchange(
                serverURL + "/stats?start={start}&end={end}&uris={uris}&unique={isUnique}",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                },
                params
        );
    }

    public Map<Long, Long> getViewsByEventIDs(List<Long> ids) {
        List<String> uris = ids.stream()
                .map(id -> "/events/" + id)
                .collect(Collectors.toList());

        Object stats = getStats(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now(),
                uris,
                true
        ).getBody();

        if (stats != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ViewStatsDto> statsRes = objectMapper.convertValue(stats, new TypeReference<>() {
            });

            Map<Long, Long> eventViews = new HashMap<>();
            statsRes.stream()
                    .filter(s -> s.getApp().equals("ewm-main-service"))
                    .forEach(s -> eventViews.put(
                            Long.parseLong(s.getUri().replaceAll("/events/", "")),
                            s.getHits()));
            return eventViews;
        }
        return Map.of();
    }
}