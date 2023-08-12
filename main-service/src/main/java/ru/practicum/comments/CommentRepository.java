package ru.practicum.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorIdAndEventId(Long userId, Long eventId);

    List<Comment> findAllByEventIdOrderByTimestampAsc(Long eventId, Pageable pageable);

    Optional<Comment> findByIdAndAuthorIdAndEventId(Long commentId, Long authorId, Long eventId);
}
