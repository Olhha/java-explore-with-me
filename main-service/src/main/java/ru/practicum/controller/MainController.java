package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
public class MainController {
    private final StatClient statClient;

    @Autowired
    public MainController(StatClient statClient) {
        this.statClient = statClient;
    }

    @GetMapping()
    public List<Object> getAllEvents(HttpServletRequest request) {
        postStats(request);

        return List.of("event1","event2");
    }

    @GetMapping("/{id}")
    public Object getEventById(@PathVariable Long id, HttpServletRequest request) {
        postStats(request);

        return "event" + id;
    }

    private void postStats(HttpServletRequest request) {
        statClient.postStats(
                "ewm-main-service",
                request.getRemoteAddr(),
                request.getRequestURI(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
