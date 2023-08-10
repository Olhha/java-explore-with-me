package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping(value = "/hit")
    public ResponseEntity<EndpointHitDto> postStats(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("StatsController: got query POST /hit with endpointHitDto = {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.postStats(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam(value = "start", required = false) String statsStart,
            @RequestParam(value = "end", required = false) String statsEnd,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        log.info("StatsController: got query GET /stats with start = {}, end = {},  uri = {}, unique = {} ",
                statsStart, statsEnd, uris, unique);
        return ResponseEntity.ok().body(statsService.getStats(statsStart, statsEnd, uris, unique));
    }
}

