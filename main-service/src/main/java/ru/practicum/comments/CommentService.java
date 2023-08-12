package ru.practicum.comments;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.EventRepository;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.UserRepository;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.PageCreator.getPage;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getAllCommentsForEventPublic(Long eventId, Integer from, Integer size) {
        return CommentMapper.toCommentDtoList(
                commentRepository.findAllByEventIdOrderByTimestampAsc(eventId, getPage(from, size)));
    }

    public CommentDto addComment(Long userId, Long eventId, CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);

        comment.setAuthor(getUserOrThrow(userId));
        comment.setEvent(getEventOrThrow(eventId));
        comment.setTimestamp(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsForEventByAuthor(Long userId, Long eventId) {
        return CommentMapper.toCommentDtoList(commentRepository.findAllByAuthorIdAndEventId(userId, eventId));
    }

    public CommentDto editCommentByAuthor(Long commentId, Long userId, Long eventId, CommentDto commentDto) {
        Comment comment = getCommentOrThrow(commentId, userId, eventId);
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteCommentByAuthor(Long commentId, Long userId, Long eventId) {
        getCommentOrThrow(commentId, userId, eventId);
        commentRepository.deleteById(commentId);
    }

    private Comment getCommentOrThrow(Long commentId, Long userId, Long eventId) {
        return commentRepository.findByIdAndAuthorIdAndEventId(commentId, userId, eventId).orElseThrow(
                () -> new NotFoundException("Event id=" + eventId + " Not Found"));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event not found id=" + eventId));
    }

    private User getUserOrThrow(Long authorId) {
        return userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("User not found id=" + authorId));
    }
}
