package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
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

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Map<String, Object> params = new HashMap<>();
        params.put("start", start.format(DateTimeFormatter.ofPattern(dateFormat)));
        params.put("end", end.format(DateTimeFormatter.ofPattern(dateFormat)));
        params.put("uris", String.join(",", uris));
        params.put("isUnique", unique);

        HttpEntity<Object> request = new HttpEntity<>(defaultHeaders());

        ResponseEntity<Object> serverResponse;

        try {
            serverResponse = restTemplate.exchange(
                    serverURL + "/stats?start={start}&end={end}&uris={uris}&unique={isUnique}",
                    HttpMethod.GET,
                    request,
                    Object.class,
                    params
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return serverResponse;
    }
}
