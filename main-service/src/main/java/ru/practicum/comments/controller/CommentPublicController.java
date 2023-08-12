package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.CommentService;
import ru.practicum.comments.dto.CommentDto;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@Slf4j
@RequiredArgsConstructor
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping()
    public List<CommentDto> getAllCommentsForEvent(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("CommentPublicController: get all comments for event id=" + eventId);
        return commentService.getAllCommentsForEventPublic(eventId, from, size);
    }
}
