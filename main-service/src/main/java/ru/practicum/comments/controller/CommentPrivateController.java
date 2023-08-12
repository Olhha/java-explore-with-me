package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.CommentService;
import ru.practicum.comments.dto.CommentDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@Slf4j
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @GetMapping()
    public List<CommentDto> getCommentsForEventAuthor(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("CommentPrivateController: get all comments from authorId={} for eventId={}",
                userId, eventId);
        return commentService.getCommentsForEventByAuthor(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentPrivateController: add a comment from authorId={} for eventId={}",
                userId, eventId);
        return commentService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("{commentId}")
    public CommentDto commentUpdate(
            @PathVariable Long commentId,
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("CommentPrivateController: update commentId ={} by authorId={} for eventId={}", commentId, userId, eventId);
        return commentService.editCommentByAuthor(commentId, userId, eventId, commentDto);
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void commentDelete(
            @PathVariable Long commentId,
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("CommentPrivateController: delete commentId ={} by authorId={} for eventId={}", commentId, userId, eventId);
        commentService.deleteCommentByAuthor(commentId, userId, eventId);
    }
}
